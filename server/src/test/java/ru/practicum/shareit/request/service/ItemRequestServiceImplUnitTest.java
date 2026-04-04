package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplUnitTest {

    @Mock
    private ItemRequestRepository repository;
    @Mock
    private UserService userService;

    @InjectMocks
    private ItemRequestServiceImpl requestService;

    @Test
    void getRequestById_whenNotFound_thenThrowsNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> requestService.getRequestById(99L));
    }

    @Test
    void validateRequestId_whenIdIsNull_thenDoesNothing() {
        assertDoesNotThrow(() -> requestService.validateRequestId(null));
    }

    @Test
    void validateRequestId_whenNotFound_thenThrowsNotFoundException() {
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> requestService.validateRequestId(99L));
    }

    @Test
    void getRequestsByRequestorId_whenEmpty_thenReturnEmptyList() {
        doNothing().when(userService).validateUserExists(1L);
        when(repository.findAllByRequestorIdOrderByCreatedDesc(1L)).thenReturn(List.of());

        List<?> result = requestService.getRequestsByRequestorId(1L);
        assertTrue(result.isEmpty());
    }
}