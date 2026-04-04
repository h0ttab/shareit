package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BookingException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @Test
    void createBooking_whenValid_thenReturnBooking() throws Exception {
        when(bookingService.createBooking(any(BookingCreateDto.class), eq(1L))).thenReturn(new BookingReturnDto());

        mockMvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BookingCreateDto())))
                .andExpect(status().isOk());
    }

    @Test
    void approveBooking_whenValid_thenReturnBooking() throws Exception {
        when(bookingService.approveBooking(1L, 1L, true)).thenReturn(new BookingReturnDto());

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header(USER_ID_HEADER, 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingById_whenValid_thenReturnBooking() throws Exception {
        when(bookingService.getBookingById(1L, 1L)).thenReturn(new BookingReturnDto());

        mockMvc.perform(get("/bookings/{bookingId}", 1L).header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByBooker_whenValid_thenReturnList() throws Exception {
        when(bookingService.getBookingsByBooker(1L, "ALL")).thenReturn(List.of());

        mockMvc.perform(get("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsByOwner_whenValid_thenReturnList() throws Exception {
        when(bookingService.getBookingsByOwner(1L, "ALL")).thenReturn(List.of());

        mockMvc.perform(get("/bookings/owner")
                        .header(USER_ID_HEADER, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void approveBooking_whenAlreadyApproved_thenStatus400() throws Exception {
        when(bookingService.approveBooking(1L, 1L, true))
                .thenThrow(new BookingException("Бронирование уже подтверждено"));

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header(USER_ID_HEADER, 1L)
                        .param("approved", "true"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Бронирование уже подтверждено"));
    }
}