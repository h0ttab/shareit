package ru.practicum.shareit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    @Qualifier("UserClient")
    RestClient userClient(@Value("#{environment.SHAREIT_SERVER_URL}") String baseUrl) {
        return RestClient.builder().baseUrl(baseUrl + "/users").build();
    }
}