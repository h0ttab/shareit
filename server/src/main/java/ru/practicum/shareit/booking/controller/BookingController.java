package ru.practicum.shareit.booking.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.service.BookingService;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingController {
    private final BookingService bookingService;
    private final String userIdHeader = "X-Sharer-User-Id";

    @PostMapping
    public BookingReturnDto createBooking(@RequestHeader(value = userIdHeader) Long bookerId,
                                          @RequestBody BookingCreateDto dto) {
        return bookingService.createBooking(dto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingReturnDto approveBooking(@PathVariable Long bookingId,
                                           @RequestParam(name = "approved") Boolean isApproved,
                                           @RequestHeader(value = userIdHeader) Long userId) {
        return bookingService.approveBooking(bookingId, userId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingReturnDto getBookingById(@PathVariable Long bookingId,
                                           @RequestHeader(value = userIdHeader) Long userId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingReturnDto> getBookingsByBooker(@RequestParam(name = "state", required = false,
                                                              defaultValue = "ALL") String state,
                                                      @RequestHeader(value = userIdHeader) Long userId) {
        return bookingService.getBookingsByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingReturnDto> getBookingsByOwner(@RequestParam(name = "state", required = false,
                                                             defaultValue = "ALL") String state,
                                                     @RequestHeader(value = userIdHeader) Long userId) {
        return bookingService.getBookingsByOwner(userId, state);
    }
}
