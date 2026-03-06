package ru.practicum.shareit.user;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto getById(Long userId);

    UserDto update(Long userId, UserDto userDto);

    void delete(Long userId);
}
