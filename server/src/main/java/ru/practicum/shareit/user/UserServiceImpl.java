package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;

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
        User user = repository.findById(userId).orElseThrow(()-> new NotFoundException("Пользователь не найден"));
        return mapper.toUserDto(user);
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User user = repository.findById(userId).orElseThrow(()-> new NotFoundException("Пользователь не найден"));
        User updatedUser = repository.save(mapper.updateUserFromDto(userDto, user));
        return mapper.toUserDto(updatedUser);
    }

    @Override
    public void delete(Long userId) {
        repository.deleteById(userId);
    }
}
