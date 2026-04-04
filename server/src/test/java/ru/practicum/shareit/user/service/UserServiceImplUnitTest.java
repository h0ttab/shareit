package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getById_whenUserNotFound_thenThrowsNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getById(99L));
    }

    @Test
    void update_whenUserNotFound_thenThrowsNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.update(99L, null));
    }

    @Test
    void delete_whenUserNotFound_thenThrowsNotFoundException() {
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> userService.delete(99L));
    }

    @Test
    void validateUserExists_whenUserNotFound_thenThrowsNotFoundException() {
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> userService.validateUserExists(99L));
    }
}