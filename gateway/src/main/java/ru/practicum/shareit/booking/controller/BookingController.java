package ru.practicum.shareit.booking.controller;

import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.exception.GlobalExceptionHandler.ErrorResponse;
import ru.practicum.shareit.exception.ServerException;

@RestController
@RequestMapping("/bookings")
@Validated
public class BookingController {
    private final RestClient restClient;
    private final String userIdHeader = "X-Sharer-User-Id";

    public BookingController(@Autowired @Qualifier("BookingsClient") RestClient client) {
        this.restClient = client;
    }

    @PostMapping
    public BookingReturnDto createBooking(@Positive @RequestHeader(value = userIdHeader) Long bookerId,
                                          @Validated @RequestBody BookingCreateDto bookingCreateDto) {
        return restClient.post().header(userIdHeader, String.valueOf(bookerId)).body(bookingCreateDto).retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((req, res) -> {
                            ErrorResponse errorResponse = ErrorResponse.readFromClientResponse(res);
                            throw new ServerException(errorResponse.statusCode(), errorResponse.error());
                        })
                )
                .body(BookingReturnDto.class);
    }

    @PatchMapping("/{bookingId}")
    public BookingReturnDto approveBooking(@Positive @PathVariable Long bookingId,
                                           @RequestParam(name = "approved") Boolean isApproved,
                                           @Positive @RequestHeader(value = userIdHeader) Long userId) {
        return restClient
                .patch()
                .uri("/{bookingId}?approved={isApproved}", bookingId, isApproved)
                .header(userIdHeader, String.valueOf(userId))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((req, res) -> {
                            ErrorResponse errorResponse = ErrorResponse.readFromClientResponse(res);
                            throw new ServerException(errorResponse.statusCode(), errorResponse.error());
                        })
                )
                .body(BookingReturnDto.class);
    }
}
