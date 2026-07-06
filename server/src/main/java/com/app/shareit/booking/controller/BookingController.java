package com.app.shareit.booking.controller;

import java.util.List;

import com.app.shareit.booking.dto.BookingCreateDto;
import com.app.shareit.booking.dto.BookingReturnDto;
import com.app.shareit.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.app.shareit.util.Constants.USER_ID_HEADER;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingReturnDto createBooking(@RequestHeader(value = USER_ID_HEADER) Long bookerId,
                                          @RequestBody BookingCreateDto dto) {
        return bookingService.createBooking(dto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingReturnDto approveBooking(@PathVariable Long bookingId,
                                           @RequestParam(name = "approved") Boolean isApproved,
                                           @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return bookingService.approveBooking(bookingId, userId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingReturnDto getBookingById(@PathVariable Long bookingId,
                                           @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingReturnDto> getBookingsByBooker(@RequestParam(name = "state", required = false,
                                                              defaultValue = "ALL") String state,
                                                      @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return bookingService.getBookingsByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingReturnDto> getBookingsByOwner(@RequestParam(name = "state", required = false,
                                                             defaultValue = "ALL") String state,
                                                     @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return bookingService.getBookingsByOwner(userId, state);
    }
}
