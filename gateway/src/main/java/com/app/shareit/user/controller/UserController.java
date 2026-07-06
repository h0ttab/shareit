package com.app.shareit.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.app.shareit.user.client.UserClient;
import com.app.shareit.user.dto.UserDto;
import com.app.shareit.user.dto.UserDto.Create;
import com.app.shareit.user.dto.UserDto.Update;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "Users", description = "User management API")
public class UserController {
    private final UserClient client;

    @Operation(summary = "Get user by ID")
    @GetMapping("/{userId}")
    public UserDto getUserById(@Positive @PathVariable Long userId) {
        return client.getUserById(userId);
    }

    @Operation(summary = "Create new user")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        return client.createUser(userDto);
    }

    @Operation(summary = "Update user")
    @PatchMapping("/{userId}")
    public UserDto updateUser(
            @Validated(Update.class) @RequestBody UserDto userDto,
            @Positive @PathVariable("userId") Long userId
    ) {
        return client.updateUser(userDto, userId);
    }

    @Operation(summary = "Delete user")
    @DeleteMapping("/{userId}")
    public void deleteUser(@Positive @PathVariable("userId") Long userId) {
        client.deleteUser(userId);
    }
}