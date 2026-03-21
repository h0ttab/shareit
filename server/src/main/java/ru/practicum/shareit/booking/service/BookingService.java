package ru.practicum.shareit.booking.service;

import java.time.LocalDateTime;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;

public interface BookingService {
    BookingReturnDto createBooking(BookingCreateDto dto, Long bookerId);

    BookingReturnDto approveBooking(Long bookingId, Long userId, Boolean isApproved);

    void validateBookingRequest(BookingCreateDto dto, Long bookerId);

    boolean isItemAvailableDuringDates(Long itemId, LocalDateTime start, LocalDateTime end);
}
