package com.app.shareit.booking.service;

import java.time.LocalDateTime;
import java.util.List;

import com.app.shareit.booking.dto.BookingCreateDto;
import com.app.shareit.booking.dto.BookingReturnDto;
import com.app.shareit.booking.model.Booking;

public interface BookingService {
    BookingReturnDto createBooking(BookingCreateDto dto, Long bookerId);

    BookingReturnDto approveBooking(Long bookingId, Long userId, Boolean isApproved);

    BookingReturnDto getBookingById(Long userId, Long bookingId);

    Booking getBookingIfExists(Long bookingId);

    void validateBookingRequest(BookingCreateDto dto, Long bookerId);

    boolean isItemAvailableDuringDates(Long itemId, LocalDateTime start, LocalDateTime end);

    List<BookingReturnDto> getBookingsByBooker(Long bookerId, String stateParam);

    List<BookingReturnDto> getBookingsByOwner(Long ownerId, String stateParam);
}
