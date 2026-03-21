package ru.practicum.shareit.booking.controller;

import java.util.List;

import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.model.BookingState;
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

    @GetMapping("/{bookingId}")
    public BookingReturnDto getBookingById(@Positive @PathVariable Long bookingId,
                                           @Positive @RequestHeader(value = userIdHeader) Long userId) {
        return restClient
                .get()
                .uri("/{bookingId}", bookingId)
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

    @GetMapping
    public List<BookingReturnDto> getBookingsByBooker(@RequestParam(name = "state", required = false,
                                                              defaultValue = "ALL") String state,
                                                      @Positive @RequestHeader(value = userIdHeader) Long userId) {
        BookingState bookingState = state == null ? BookingState.ALL : BookingState.from(state);
        return restClient
                .get()
                .uri("?state={state}", bookingState)
                .header(userIdHeader, String.valueOf(userId))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((req, res) -> {
                            ErrorResponse errorResponse = ErrorResponse.readFromClientResponse(res);
                            throw new ServerException(errorResponse.statusCode(), errorResponse.error());
                        })
                )
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @GetMapping("/owner")
    public List<BookingReturnDto> getBookingsByOwner(@RequestParam(name = "state", required = false,
                                                              defaultValue = "ALL") String state,
                                                      @Positive @RequestHeader(value = userIdHeader) Long userId) {
        BookingState bookingState = state == null ? BookingState.ALL : BookingState.from(state);
        return restClient
                .get()
                .uri("/owner?state={state}", bookingState)
                .header(userIdHeader, String.valueOf(userId))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((req, res) -> {
                            ErrorResponse errorResponse = ErrorResponse.readFromClientResponse(res);
                            throw new ServerException(errorResponse.statusCode(), errorResponse.error());
                        })
                )
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
