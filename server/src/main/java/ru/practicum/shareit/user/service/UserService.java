package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto getById(Long userId);

    UserDto update(Long userId, UserDto userDto);

    void delete(Long userId);

    void validateUserExists(Long userId);
}
