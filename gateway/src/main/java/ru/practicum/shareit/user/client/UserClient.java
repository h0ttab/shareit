package ru.practicum.shareit.user.client;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserClient {
    UserDto getUserById(Long userId);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, Long userId);

    void deleteUser(Long userId);
}
