package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserClient userClient;

    @Test
    void createUser_whenValid_thenStatus201() throws Exception {
        UserDto dto = new UserDto(null, "Ivan", "ivan@mail.com");
        UserDto responseDto = new UserDto(1L, "Ivan", "ivan@mail.com");

        when(userClient.createUser(any(UserDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Ivan"));

        verify(userClient, times(1)).createUser(any(UserDto.class));
    }

    @Test
    void createUser_whenInvalidEmail_thenStatus400() throws Exception {
        UserDto dto = new UserDto(null, "Ivan", "invalid-email");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());

        verify(userClient, never()).createUser(any());
    }

    @Test
    void getUserById_whenValid_thenStatus200() throws Exception {
        UserDto responseDto = new UserDto(1L, "Ivan", "ivan@mail.com");
        when(userClient.getUserById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(userClient, times(1)).getUserById(1L);
    }

    @Test
    void updateUser_whenValid_thenStatus200() throws Exception {
        UserDto dto = new UserDto();
        dto.setName("Ivan Updated");

        when(userClient.updateUser(any(UserDto.class), eq(1L))).thenReturn(dto);

        mockMvc.perform(patch("/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(userClient, times(1)).updateUser(any(), eq(1L));
    }

    @Test
    void deleteUser_whenValid_thenStatus200() throws Exception {
        mockMvc.perform(delete("/users/{userId}", 1L))
                .andExpect(status().isOk());

        verify(userClient, times(1)).deleteUser(1L);
    }

    @Test
    void getUserById_whenUnexpectedError_thenStatus500() throws Exception {
        when(userClient.getUserById(1L)).thenThrow(new RuntimeException("Unexpected"));

        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Внутренняя ошибка сервера"));
    }
}