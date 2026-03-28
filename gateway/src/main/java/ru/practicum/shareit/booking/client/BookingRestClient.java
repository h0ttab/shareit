package ru.practicum.shareit.booking.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.model.BookingState;

import static ru.practicum.shareit.util.Constants.userIdHeader;

@Component
@Primary
public class BookingRestClient implements BookingClient {
    private final RestClient restClient;

    public BookingRestClient(@Autowired @Qualifier("BookingsClient") RestClient client) {
        this.restClient = client;
    }

    @Override
    public BookingReturnDto createBooking(Long bookerId, BookingCreateDto bookingCreateDto) {
        return restClient
                .post()
                .header(userIdHeader, String.valueOf(bookerId))
                .body(bookingCreateDto)
                .retrieve()
                .body(BookingReturnDto.class);
    }

    @Override
    public BookingReturnDto approveBooking(Long bookingId, Boolean isApproved, Long userId) {
        return restClient
                .patch()
                .uri("/{bookingId}?approved={isApproved}", bookingId, isApproved)
                .header(userIdHeader, String.valueOf(userId))
                .retrieve()
                .body(BookingReturnDto.class);
    }

    @Override
    public BookingReturnDto getBookingById(Long bookingId, Long userId) {
        return restClient
                .get()
                .uri("/{bookingId}", bookingId)
                .header(userIdHeader, String.valueOf(userId))
                .retrieve()
                .body(BookingReturnDto.class);
    }

    @Override
    public List<BookingReturnDto> getBookingsByBooker(BookingState bookingState, Long userId) {
        return restClient
                .get()
                .uri("?state={state}", bookingState)
                .header(userIdHeader, String.valueOf(userId))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @Override
    public List<BookingReturnDto> getBookingsByOwner(BookingState bookingState, Long userId) {
        return restClient
                .get()
                .uri("/owner?state={state}", bookingState)
                .header(userIdHeader, String.valueOf(userId))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
