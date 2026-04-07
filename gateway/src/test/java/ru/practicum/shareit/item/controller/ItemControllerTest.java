package ru.practicum.shareit.item.controller;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    private static final String HEADER = "X-Sharer-User-Id";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemClient itemClient;

    @Test
    void createItem_whenValid_thenStatus201() throws Exception {
        ItemDto dto = new ItemDto(null, "Item", "Desc", true, null, null, null, null);
        when(itemClient.createItem(eq(1L), any(ItemDto.class))).thenReturn(dto);

        mockMvc.perform(post("/items")
                        .header(HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(itemClient, times(1)).createItem(eq(1L), any());
    }

    @Test
    void createItem_whenMissingHeader_thenStatus400() throws Exception {
        ItemDto dto = new ItemDto(null, "Item", "Desc", true, null, null, null, null);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest()); // Ошибка из-за отсутствия обязательного заголовка

        verify(itemClient, never()).createItem(any(), any());
    }

    @Test
    void getAllItemsByOwner_whenValid_thenStatus200() throws Exception {
        when(itemClient.getAllItemsByOwner(1L)).thenReturn(List.of(new ItemDto()));

        mockMvc.perform(get("/items").header(HEADER, 1L))
                .andExpect(status().isOk());

        verify(itemClient, times(1)).getAllItemsByOwner(1L);
    }

    @Test
    void searchAvailableItems_whenValid_thenStatus200() throws Exception {
        when(itemClient.searchAvailableItems("text")).thenReturn(List.of());

        mockMvc.perform(get("/items/search").param("text", "text"))
                .andExpect(status().isOk());

        verify(itemClient, times(1)).searchAvailableItems("text");
    }

    @Test
    void createComment_whenValid_thenStatus201() throws Exception {
        CommentDto dto = new CommentDto("Nice item!", null, null, null);
        when(itemClient.createComment(eq(1L), eq(2L), any())).thenReturn(dto);

        mockMvc.perform(post("/items/{itemId}/comment", 2L)
                        .header(HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void getItemById_whenValid_thenStatus200() throws Exception {
        when(itemClient.getItemById(1L, 1L)).thenReturn(new ItemDto());

        mockMvc.perform(get("/items/{itemId}", 1L).header(HEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem_whenValid_thenStatus200() throws Exception {
        when(itemClient.updateItem(eq(1L), eq(1L), any())).thenReturn(new ItemDto());

        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header(HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ItemDto())))
                .andExpect(status().isOk());
    }

    @Test
    void deleteItem_whenValid_thenStatus200() throws Exception {
        mockMvc.perform(delete("/items/{itemId}", 1L).header(HEADER, 1L))
                .andExpect(status().isOk());
        verify(itemClient, times(1)).deleteItem(1L, 1L);
    }
}