package com.app.shareit.user.service;

import com.app.shareit.user.dto.UserDto;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto getById(Long userId);

    UserDto update(Long userId, UserDto userDto);

    void delete(Long userId);

    void validateUserExists(Long userId);
}
