package ru.practicum.shareit.booking.client;

import java.util.List;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.model.BookingState;

public interface BookingClient {
    BookingReturnDto createBooking(Long bookerId, BookingCreateDto bookingCreateDto);

    BookingReturnDto approveBooking(Long bookingId, Boolean isApproved, Long userId);

    BookingReturnDto getBookingById(Long bookingId, Long userId);

    List<BookingReturnDto> getBookingsByBooker(BookingState state, Long userId);

    List<BookingReturnDto> getBookingsByOwner(BookingState state, Long userId);
}
