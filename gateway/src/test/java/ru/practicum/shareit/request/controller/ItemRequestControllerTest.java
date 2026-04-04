package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestFullDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestClient itemRequestClient;

    private static final String HEADER = "X-Sharer-User-Id";

    @Test
    void createRequest_whenValid_thenStatus201() throws Exception {
        ItemRequestCreateDto dto = new ItemRequestCreateDto("Need an item");
        when(itemRequestClient.createRequest(any(), eq(1L))).thenReturn(new ItemRequestFullDto());

        mockMvc.perform(post("/requests")
                        .header(HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(itemRequestClient, times(1)).createRequest(any(), eq(1L));
    }

    @Test
    void getRequestsByRequestorId_whenValid_thenStatus200() throws Exception {
        when(itemRequestClient.getRequestsByRequestorId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/requests").header(HEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRequests_whenValid_thenStatus200() throws Exception {
        when(itemRequestClient.getAllRequests(1L)).thenReturn(List.of());

        mockMvc.perform(get("/requests/all").header(HEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestById_whenValid_thenStatus200() throws Exception {
        when(itemRequestClient.getRequestById(2L, 1L)).thenReturn(new ItemRequestFullDto());

        mockMvc.perform(get("/requests/{requestId}", 2L)
                        .header(HEADER, 1L))
                .andExpect(status().isOk());
    }
}