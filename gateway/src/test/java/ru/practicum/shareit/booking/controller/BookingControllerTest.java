package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    private static final String HEADER = "X-Sharer-User-Id";

    @Test
    void createBooking_whenValid_thenStatus201() throws Exception {
        BookingCreateDto dto = new BookingCreateDto(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        when(bookingClient.createBooking(eq(1L), any())).thenReturn(new BookingReturnDto());

        mockMvc.perform(post("/bookings")
                        .header(HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(bookingClient, times(1)).createBooking(eq(1L), any());
    }

    @Test
    void getBookingsByBooker_whenValidState_thenStatus200() throws Exception {
        when(bookingClient.getBookingsByBooker(BookingState.ALL, 1L)).thenReturn(List.of());

        mockMvc.perform(get("/bookings")
                        .header(HEADER, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByBooker_whenInvalidState_thenStatus400() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header(HEADER, 1L)
                        .param("state", "UNSUPPORTED_STATE"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());

        verify(bookingClient, never()).getBookingsByBooker(any(), any());
    }

    @Test
    void approveBooking_whenValid_thenStatus200() throws Exception {
        when(bookingClient.approveBooking(1L, true, 1L)).thenReturn(new BookingReturnDto());

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header(HEADER, 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk());

        verify(bookingClient, times(1)).approveBooking(1L, true, 1L);
    }

    @Test
    void getBookingById_whenValid_thenStatus200() throws Exception {
        when(bookingClient.getBookingById(1L, 1L)).thenReturn(new BookingReturnDto());

        mockMvc.perform(get("/bookings/{bookingId}", 1L).header(HEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_whenValid_thenStatus200() throws Exception {
        when(bookingClient.getBookingsByOwner(BookingState.ALL, 1L)).thenReturn(List.of());

        mockMvc.perform(get("/bookings/owner")
                        .header(HEADER, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }
}