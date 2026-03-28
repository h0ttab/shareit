package ru.practicum.shareit.user.controller;

import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDto.Create;
import ru.practicum.shareit.user.dto.UserDto.Update;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final RestClient restClient;

    public UserController(@Autowired @Qualifier("UserClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@Positive @PathVariable Long userId) {
        return restClient
                .get()
                .uri("/{userId}", userId)
                .retrieve()
                .body(UserDto.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        return restClient
                .post()
                .body(userDto)
                .retrieve()
                .body(UserDto.class);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(
            @Validated(Update.class) @RequestBody UserDto userDto,
            @Positive @PathVariable("userId") Long userId
    ) {
        return restClient
                .patch()
                .uri("/{userId}", userId)
                .body(userDto)
                .retrieve()
                .body(UserDto.class);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@Positive @PathVariable("userId") Long userId) {
        restClient
                .delete()
                .uri("/{userId}", userId)
                .retrieve();
    }
}
