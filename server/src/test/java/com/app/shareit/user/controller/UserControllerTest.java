package com.app.shareit.user.controller;

import org.junit.jupiter.api.Test;

import com.app.shareit.exception.NotFoundException;
import com.app.shareit.user.dto.UserDto;
import com.app.shareit.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void getUserById_whenValid_thenReturnUser() throws Exception {
        UserDto dto = new UserDto(1L, "Ivan", "ivan@mail.com");
        when(userService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void createUser_whenValid_thenReturnUser() throws Exception {
        UserDto dto = new UserDto(null, "Ivan", "ivan@mail.com");
        UserDto savedDto = new UserDto(1L, "Ivan", "ivan@mail.com");
        when(userService.create(any(UserDto.class))).thenReturn(savedDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void updateUser_whenValid_thenReturnUser() throws Exception {
        UserDto dto = new UserDto(null, "Ivan Updated", null);
        when(userService.update(eq(1L), any(UserDto.class))).thenReturn(dto);

        mockMvc.perform(patch("/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_whenValid_thenStatus200() throws Exception {
        mockMvc.perform(delete("/users/{userId}", 1L))
                .andExpect(status().isOk());
        verify(userService, times(1)).delete(1L);
    }

    @Test
    void getUserById_whenUserNotFound_thenStatus404() throws Exception {
        when(userService.getById(99L)).thenThrow(new NotFoundException("Пользователь не найден"));

        mockMvc.perform(get("/users/{userId}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Пользователь не найден"));
    }

    @Test
    void createUser_whenDuplicateEmail_thenStatus409() throws Exception {
        when(userService.create(any())).thenThrow(new DataIntegrityViolationException("uq_user_email"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserDto())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Такой email уже зарегистрирован"));
    }

    @Test
    void createUser_whenOtherDataIntegrityException_thenStatus409() throws Exception {
        when(userService.create(any())).thenThrow(new DataIntegrityViolationException("Other constraint"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserDto())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Ошибка при сохранении данных: Other constraint"));
    }
}