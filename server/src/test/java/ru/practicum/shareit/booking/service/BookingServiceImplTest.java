package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class BookingServiceImplTest {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    private Long ownerId;
    private Long bookerId;
    private Long itemId;

    @BeforeEach
    void setUp() {
        ownerId = userService.create(new UserDto(null, "Owner", "owner@mail.com")).getId();
        bookerId = userService.create(new UserDto(null, "Booker", "booker@mail.com")).getId();
        itemId = itemService.create(new ItemDto(null, "Item", "Desc", true, null, null, null, null), ownerId).getId();
    }

    @Test
    void testBookingLifecycle_Create_Approve_GetStates() {
        BookingCreateDto dto = new BookingCreateDto(itemId, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        BookingReturnDto created = bookingService.createBooking(dto, bookerId);
        Long bookingId = created.getId();

        assertEquals(BookingStatus.WAITING, created.getStatus());

        BookingReturnDto approved = bookingService.approveBooking(bookingId, ownerId, true);
        assertEquals(BookingStatus.APPROVED, approved.getStatus());

        List<BookingReturnDto> allBooker = bookingService.getBookingsByBooker(bookerId, "ALL");
        assertEquals(1, allBooker.size());

        List<BookingReturnDto> futureBooker = bookingService.getBookingsByBooker(bookerId, "FUTURE");
        assertEquals(1, futureBooker.size());

        List<BookingReturnDto> pastBooker = bookingService.getBookingsByBooker(bookerId, "PAST");
        assertEquals(0, pastBooker.size());

        List<BookingReturnDto> allOwner = bookingService.getBookingsByOwner(ownerId, "ALL");
        assertEquals(1, allOwner.size());

        BookingReturnDto getById = bookingService.getBookingById(bookerId, bookingId);
        assertEquals(bookingId, getById.getId());
    }

    @Test
    void getBookingsByBooker_withRejectedState() {
        BookingCreateDto dto = new BookingCreateDto(itemId, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        Long bookingId = bookingService.createBooking(dto, bookerId).getId();

        bookingService.approveBooking(bookingId, ownerId, false);

        List<BookingReturnDto> rejected = bookingService.getBookingsByBooker(bookerId, "REJECTED");
        assertEquals(1, rejected.size());
    }
}