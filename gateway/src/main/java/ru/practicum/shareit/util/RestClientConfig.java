package ru.practicum.shareit.util;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @Qualifier("UserClient")
    RestClient userClient(@Value("${shareit-server}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl + "/users").build();
    }

    @Bean
    @Qualifier("ItemClient")
    RestClient itemClient(@Value("${shareit-server}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl + "/items").build();
    }

    @Bean
    @Qualifier("BookingsClient")
    RestClient bookingsClient(@Value("${shareit-server}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl + "/bookings").build();
    }

    @Bean
    @Qualifier("RequestsClient")
    RestClient requestsClient(@Value("${shareit-server}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl + "/requests").build();
    }
}