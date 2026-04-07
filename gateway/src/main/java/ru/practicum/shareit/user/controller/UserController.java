package ru.practicum.shareit.user.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDto.Create;
import ru.practicum.shareit.user.dto.UserDto.Update;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {
    private final UserClient client;

    @GetMapping("/{userId}")
    public UserDto getUserById(@Positive @PathVariable Long userId) {
        return client.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        return client.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(
            @Validated(Update.class) @RequestBody UserDto userDto,
            @Positive @PathVariable("userId") Long userId
    ) {
        return client.updateUser(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@Positive @PathVariable("userId") Long userId) {
        client.deleteUser(userId);
    }
}
