package ru.practicum.shareit.request.controller;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestFullDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    void createRequest_whenValid_thenReturnDto() throws Exception {
        when(itemRequestService.createRequest(any(ItemRequestCreateDto.class), eq(1L)))
                .thenReturn(new ItemRequestFullDto());

        mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ItemRequestCreateDto("Desc"))))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestsByRequestorId_whenValid_thenReturnList() throws Exception {
        when(itemRequestService.getRequestsByRequestorId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/requests").header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRequests_whenValid_thenReturnList() throws Exception {
        when(itemRequestService.getAllRequests(1L)).thenReturn(List.of());

        mockMvc.perform(get("/requests/all").header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestById_whenValid_thenReturnDto() throws Exception {
        when(itemRequestService.getRequestById(1L)).thenReturn(new ItemRequestFullDto());

        mockMvc.perform(get("/requests/{requestId}", 1L))
                .andExpect(status().isOk());
    }
}