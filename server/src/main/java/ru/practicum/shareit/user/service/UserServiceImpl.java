package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@Primary
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDto create(UserDto userDto) {
        User newUser = repository.save(mapper.fromUserDto(userDto));
        return mapper.toUserDto(newUser);
    }

    @Override
    public UserDto getById(Long userId) {
        User user = repository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return mapper.toUserDto(user);
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User user = repository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        User updatedUser = repository.save(mapper.updateUserFromDto(userDto, user));
        return mapper.toUserDto(updatedUser);
    }

    @Override
    public void delete(Long userId) {
        repository.deleteById(userId);
    }

    @Override
    public boolean existsById(Long userId) {
        return repository.existsById(userId);
    }
}
