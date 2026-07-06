package com.app.shareit.user.service;

import com.app.shareit.exception.NotFoundException;
import com.app.shareit.user.dto.UserDto;
import com.app.shareit.user.dto.mapper.UserMapper;
import com.app.shareit.user.model.User;
import com.app.shareit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDto create(UserDto userDto) {
        User newUser = repository.save(mapper.fromUserDto(userDto));
        return mapper.toUserDto(newUser);
    }

    @Override
    @Transactional(readOnly = true)
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
        validateUserExists(userId);
        repository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public void validateUserExists(Long userId) {
        if (!repository.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь с id=%d не найден", userId));
        }
    }
}
