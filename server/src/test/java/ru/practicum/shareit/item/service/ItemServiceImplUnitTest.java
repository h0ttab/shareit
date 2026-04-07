package ru.practicum.shareit.item.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OwnerMismatchException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplUnitTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void getById_whenItemNotFound_thenThrowsNotFoundException() {
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.getById(99L, 1L));
    }

    @Test
    void getAllByOwnerId_whenNoItems_thenReturnEmptyList() {
        doNothing().when(userService).validateUserExists(1L);
        when(itemRepository.findByOwnerId(1L)).thenReturn(List.of());

        assertTrue(itemService.getAllByOwnerId(1L).isEmpty());
    }

    @Test
    void searchAvailableItems_whenQueryIsBlank_thenReturnEmptyList() {
        assertTrue(itemService.searchAvailableItems("   ").isEmpty());
    }

    @Test
    void validateItemOwnership_whenNotOwner_thenThrowsOwnerMismatchException() {
        doNothing().when(userService).validateUserExists(1L);
        when(itemRepository.findOwnerIdByItemId(99L)).thenReturn(2L);

        assertThrows(OwnerMismatchException.class, () -> itemService.validateItemOwnership(99L, 1L));
    }
}