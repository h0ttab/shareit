package ru.practicum.shareit.item.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplUnitTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void createComment_whenItemNotFound_thenThrowsNotFoundException() {
        doNothing().when(userService).validateUserExists(1L);
        when(itemRepository.existsById(99L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> commentService.createComment(null, 99L, 1L));
    }

    @Test
    void createComment_whenUserDidNotBookItem_thenThrowsBookingException() {
        doNothing().when(userService).validateUserExists(1L);
        when(itemRepository.existsById(99L)).thenReturn(true);
        when(bookingRepository.existsByBookerIdAndItemIdAndStatusAndEndDateBefore(
                eq(1L), eq(99L), eq(BookingStatus.APPROVED), any()
        )).thenReturn(false);

        assertThrows(BookingException.class, () -> commentService.createComment(null, 99L, 1L));
    }

    @Test
    void findByItemIdIn_whenNoItems_thenReturnEmptyList() {
        when(commentRepository.findByItemIdIn(List.of())).thenReturn(List.of());
        assertTrue(commentService.findByItemIdIn(List.of()).isEmpty());
    }
}