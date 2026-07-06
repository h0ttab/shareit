package com.app.shareit.booking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.app.shareit.booking.client.BookingClient;
import com.app.shareit.booking.dto.BookingCreateDto;
import com.app.shareit.booking.dto.BookingReturnDto;
import com.app.shareit.booking.model.BookingState;
import static com.app.shareit.util.Constants.USER_ID_HEADER;

@Validated
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/bookings")
@Tag(name = "Bookings", description = "Item bookings API")
public class BookingController {
    private final BookingClient client;

    @Operation(summary = "Create booking request")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingReturnDto createBooking(@Positive @RequestHeader(value = USER_ID_HEADER) Long bookerId,
                                          @Validated @RequestBody BookingCreateDto bookingCreateDto) {
        return client.createBooking(bookerId, bookingCreateDto);
    }

    @Operation(summary = "Approve or reject booking (for item owner)")
    @PatchMapping("/{bookingId}")
    public BookingReturnDto approveBooking(@Positive @PathVariable Long bookingId,
                                           @RequestParam(name = "approved") Boolean isApproved,
                                           @Positive @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return client.approveBooking(bookingId, isApproved, userId);
    }

    @Operation(summary = "Get booking data by booking id")
    @GetMapping("/{bookingId}")
    public BookingReturnDto getBookingById(@Positive @PathVariable Long bookingId,
                                           @Positive @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return client.getBookingById(bookingId, userId);
    }

    @Operation(summary = "Get current user's bookings")
    @GetMapping
    public List<BookingReturnDto> getBookingsByBooker(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                      @Positive @RequestHeader(value = USER_ID_HEADER) Long userId) {
        BookingState bookingState = BookingState.from(state);
        return client.getBookingsByBooker(bookingState, userId);
    }

    @Operation(summary = "Get bookings for all items owned by current user")
    @GetMapping("/owner")
    public List<BookingReturnDto> getBookingsByOwner(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                                     @Positive @RequestHeader(value = USER_ID_HEADER) Long userId) {
        BookingState bookingState = BookingState.from(state);
        return client.getBookingsByOwner(bookingState, userId);
    }
}