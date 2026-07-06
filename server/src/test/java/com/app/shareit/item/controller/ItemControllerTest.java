package com.app.shareit.item.controller;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.app.shareit.exception.ItemUnavailableException;
import com.app.shareit.exception.OwnerMismatchException;
import com.app.shareit.item.dto.CommentDto;
import com.app.shareit.item.dto.ItemDto;
import com.app.shareit.item.service.CommentService;
import com.app.shareit.item.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.app.shareit.util.Constants.USER_ID_HEADER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @MockBean
    private CommentService commentService;

    @Test
    void getAllItemsByOwner_whenValid_thenReturnList() throws Exception {
        when(itemService.getAllByOwnerId(1L)).thenReturn(List.of(new ItemDto()));

        mockMvc.perform(get("/items").header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getItemById_whenValid_thenReturnItem() throws Exception {
        when(itemService.getById(1L, 1L)).thenReturn(new ItemDto());

        mockMvc.perform(get("/items/{itemId}", 1L).header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void searchAvailableItems_whenValid_thenReturnList() throws Exception {
        when(itemService.searchAvailableItems("query")).thenReturn(List.of(new ItemDto()));

        mockMvc.perform(get("/items/search").param("text", "query"))
                .andExpect(status().isOk());
    }

    @Test
    void createItem_whenValid_thenReturnItem() throws Exception {
        when(itemService.create(any(ItemDto.class), eq(1L))).thenReturn(new ItemDto());

        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ItemDto())))
                .andExpect(status().isOk());
    }

    @Test
    void createComment_whenValid_thenReturnComment() throws Exception {
        when(commentService.createComment(any(CommentDto.class), eq(1L), eq(1L))).thenReturn(new CommentDto());

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CommentDto())))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem_whenValid_thenReturnItem() throws Exception {
        when(itemService.update(eq(1L), any(ItemDto.class), eq(1L))).thenReturn(new ItemDto());

        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ItemDto())))
                .andExpect(status().isOk());
    }

    @Test
    void deleteItem_whenValid_thenStatus200() throws Exception {
        mockMvc.perform(delete("/items/{itemId}", 1L).header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());
        verify(itemService, times(1)).delete(1L, 1L);
    }

    @Test
    void deleteItem_whenNotOwner_thenStatus403() throws Exception {
        doThrow(new OwnerMismatchException("Not owner")).when(itemService).delete(1L, 2L);

        mockMvc.perform(delete("/items/{itemId}", 1L).header(USER_ID_HEADER, 2L))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Not owner"));
    }

    @Test
    void getItem_whenUnavailable_thenStatus409() throws Exception {
        when(itemService.getById(1L, 1L)).thenThrow(new ItemUnavailableException("Unavailable"));

        mockMvc.perform(get("/items/{itemId}", 1L).header(USER_ID_HEADER, 1L))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Unavailable"));
    }
}