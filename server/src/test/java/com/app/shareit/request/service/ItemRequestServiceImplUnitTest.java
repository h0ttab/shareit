package com.app.shareit.request.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.app.shareit.exception.NotFoundException;
import com.app.shareit.request.repository.ItemRequestRepository;
import com.app.shareit.user.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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

    @Test
    void validateRequestId_whenIdExists_thenSuccess() {
        when(repository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> requestService.validateRequestId(1L));
    }
}