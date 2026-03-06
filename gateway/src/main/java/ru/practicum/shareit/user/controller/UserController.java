package ru.practicum.shareit.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.exception.GlobalExceptionHandler.ErrorResponse;
import ru.practicum.shareit.exception.ServerException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDto.Create;
import ru.practicum.shareit.user.dto.UserDto.Update;

@RestController
@RequestMapping("/users")
public class UserController {
    private final RestClient restClient;

    public UserController(@Autowired @Qualifier("UserClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        return restClient.get().uri("/{userId}", userId).retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((req, res) -> {
                            ErrorResponse errorResponse = ErrorResponse.readFromClientResponse(res);
                            throw new ServerException(errorResponse.statusCode(), errorResponse.errorMessage());
                        })
                )
                .body(UserDto.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        return restClient
                .post().body(userDto).retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((req, res) -> {
                            ErrorResponse errorResponse = ErrorResponse.readFromClientResponse(res);
                            throw new ServerException(errorResponse.statusCode(), errorResponse.errorMessage());
                        })
                )
                .body(UserDto.class);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(
            @Validated(Update.class) @RequestBody UserDto userDto,
            @PathVariable("userId") Long userId
    ) {
        return restClient.patch().uri("/{userId}", userId).body(userDto).retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((req, res) -> {
                            ErrorResponse errorResponse = ErrorResponse.readFromClientResponse(res);
                            throw new ServerException(errorResponse.statusCode(), errorResponse.errorMessage());
                        })
                )
                .body(UserDto.class);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        restClient.delete().uri("/{userId}", userId);
    }
}
