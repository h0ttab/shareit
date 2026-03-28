package ru.practicum.shareit.util;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.exception.GlobalExceptionHandler.*;
import ru.practicum.shareit.exception.ServerException;

@Configuration
public class RestClientConfig {

    private final RestClient.ResponseSpec.ErrorHandler errorHandler = (req, res) -> {
        ErrorResponse errorResponse = ErrorResponse.readFromClientResponse(res);
        throw new ServerException(errorResponse.statusCode(), errorResponse.error());
    };

    @Bean
    @Qualifier("UserClient")
    RestClient userClient(@Value("${shareit-server}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl + "/users")
                .defaultStatusHandler(HttpStatusCode::isError, errorHandler)
                .build();
    }

    @Bean
    @Qualifier("ItemClient")
    RestClient itemClient(@Value("${shareit-server}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl + "/items")
                .defaultStatusHandler(HttpStatusCode::isError, errorHandler)
                .build();
    }

    @Bean
    @Qualifier("BookingsClient")
    RestClient bookingsClient(@Value("${shareit-server}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl + "/bookings")
                .defaultStatusHandler(HttpStatusCode::isError, errorHandler)
                .build();
    }

    @Bean
    @Qualifier("RequestsClient")
    RestClient requestsClient(@Value("${shareit-server}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl + "/requests")
                .defaultStatusHandler(HttpStatusCode::isError, errorHandler)
                .build();
    }
}