package ru.practicum.shareit.user.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.user.dto.UserDto;

@Primary
@Component
public class UserRestClient implements UserClient {
    private final RestClient restClient;

    public UserRestClient(@Autowired @Qualifier("UserClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public UserDto getUserById(Long userId) {
        return restClient
                .get()
                .uri("/{userId}", userId)
                .retrieve()
                .body(UserDto.class);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return restClient
                .post()
                .body(userDto)
                .retrieve()
                .body(UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        return restClient
                .patch()
                .uri("/{userId}", userId)
                .body(userDto)
                .retrieve()
                .body(UserDto.class);
    }

    @Override
    public void deleteUser(Long userId) {
        restClient
                .delete()
                .uri("/{userId}", userId)
                .retrieve();
    }
}
