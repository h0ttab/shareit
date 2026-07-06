package com.app.shareit.booking.service;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.app.shareit.booking.dto.BookingCreateDto;
import com.app.shareit.booking.model.Booking;
import com.app.shareit.booking.model.BookingStatus;
import com.app.shareit.booking.repository.BookingRepository;
import com.app.shareit.exception.*;
import com.app.shareit.item.dto.ItemDto;
import com.app.shareit.item.model.Item;
import com.app.shareit.item.service.ItemService;
import com.app.shareit.user.model.User;
import com.app.shareit.user.service.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplUnitTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void getBookingIfExists_whenNotFound_thenThrowsNotFoundException() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookingService.getBookingIfExists(99L));
    }

    @Test
    void approveBooking_whenAlreadyApproved_thenThrowsBookingException() {
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(99L)).thenReturn(Optional.of(booking));

        assertThrows(BookingException.class, () -> bookingService.approveBooking(99L, 1L, true));
    }

    @Test
    void approveBooking_whenNotOwner_thenThrowsOwnerMismatchException() {
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.WAITING);
        Item item = new Item();
        User owner = new User();
        owner.setId(2L);
        item.setOwner(owner);
        booking.setItem(item);

        when(bookingRepository.findById(99L)).thenReturn(Optional.of(booking));

        assertThrows(OwnerMismatchException.class, () -> bookingService.approveBooking(99L, 1L, true));
    }

    @Test
    void getBookingById_whenNotBookerAndNotOwner_thenThrowsOwnerMismatchException() {
        doNothing().when(userService).validateUserExists(3L);
        Booking booking = new Booking();

        Item item = new Item();
        User owner = new User();
        owner.setId(2L);
        item.setOwner(owner);
        booking.setItem(item);

        User booker = new User();
        booker.setId(1L);
        booking.setBooker(booker);

        when(bookingRepository.findById(99L)).thenReturn(Optional.of(booking));

        assertThrows(OwnerMismatchException.class, () -> bookingService.getBookingById(3L, 99L));
    }

    @Test
    void validateBookingRequest_whenOwnerBooksOwnItem_thenThrowsOwnerMismatchException() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(99L);
        when(itemService.getById(99L, 1L)).thenReturn(itemDto);
        doNothing().when(userService).validateUserExists(1L);
        when(itemService.getOwnerIdByItemId(99L)).thenReturn(1L);

        BookingCreateDto dto = new BookingCreateDto();
        dto.setItemId(99L);

        assertThrows(OwnerMismatchException.class, () -> bookingService.validateBookingRequest(dto, 1L));
    }

    @Test
    void validateBookingRequest_whenItemUnavailable_thenThrowsItemUnavailableException() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(99L);
        itemDto.setAvailable(false);

        when(itemService.getById(99L, 1L)).thenReturn(itemDto);
        doNothing().when(userService).validateUserExists(1L);
        when(itemService.getOwnerIdByItemId(99L)).thenReturn(2L);

        BookingCreateDto dto = new BookingCreateDto();
        dto.setItemId(99L);

        assertThrows(ItemUnavailableException.class, () -> bookingService.validateBookingRequest(dto, 1L));
    }

    @Test
    void validateBookingRequest_whenDatesOverlapping_thenThrowsItemUnavailableException() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(99L);
        itemDto.setAvailable(true);

        when(itemService.getById(99L, 1L)).thenReturn(itemDto);
        doNothing().when(userService).validateUserExists(1L);
        when(itemService.getOwnerIdByItemId(99L)).thenReturn(2L);

        when(bookingRepository.isItemAvailableDuringDates(eq(99L), any(), any())).thenReturn(false);

        BookingCreateDto dto = new BookingCreateDto();
        dto.setItemId(99L);

        assertThrows(ItemUnavailableException.class, () -> bookingService.validateBookingRequest(dto, 1L));
    }
}