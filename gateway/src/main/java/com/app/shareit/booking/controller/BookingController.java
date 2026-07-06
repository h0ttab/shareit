package com.app.shareit.booking.controller;

import java.util.List;

import com.app.shareit.booking.client.BookingClient;
import com.app.shareit.booking.dto.BookingCreateDto;
import com.app.shareit.booking.dto.BookingReturnDto;
import com.app.shareit.booking.model.BookingState;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.app.shareit.util.Constants.USER_ID_HEADER;

@Validated
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/bookings")
public class BookingController {
    private final BookingClient client;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingReturnDto createBooking(@Positive @RequestHeader(value = USER_ID_HEADER) Long bookerId,
                                          @Validated @RequestBody BookingCreateDto bookingCreateDto) {
        return client.createBooking(bookerId, bookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingReturnDto approveBooking(@Positive @PathVariable Long bookingId,
                                           @RequestParam(name = "approved") Boolean isApproved,
                                           @Positive @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return client.approveBooking(bookingId, isApproved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingReturnDto getBookingById(@Positive @PathVariable Long bookingId,
                                           @Positive @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return client.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingReturnDto> getBookingsByBooker(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                      @Positive @RequestHeader(value = USER_ID_HEADER) Long userId) {
        BookingState bookingState = BookingState.from(state);
        return client.getBookingsByBooker(bookingState, userId);
    }

    @GetMapping("/owner")
    public List<BookingReturnDto> getBookingsByOwner(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                     @Positive @RequestHeader(value = USER_ID_HEADER) Long userId) {
        BookingState bookingState = BookingState.from(state);
        return client.getBookingsByOwner(bookingState, userId);
    }
}
