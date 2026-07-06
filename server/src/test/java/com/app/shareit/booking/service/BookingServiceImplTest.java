package com.app.shareit.booking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.app.shareit.booking.dto.BookingCreateDto;
import com.app.shareit.booking.dto.BookingReturnDto;
import com.app.shareit.booking.model.BookingStatus;
import com.app.shareit.item.dto.ItemDto;
import com.app.shareit.item.service.ItemService;
import com.app.shareit.user.dto.UserDto;
import com.app.shareit.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

    @Test
    void getBookings_forAllStates_bothRoles() {
        BookingCreateDto pastDto = new BookingCreateDto(itemId, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(4));
        Long pastId = bookingService.createBooking(pastDto, bookerId).getId();
        bookingService.approveBooking(pastId, ownerId, true);

        BookingCreateDto currentDto = new BookingCreateDto(itemId, LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
        Long currentId = bookingService.createBooking(currentDto, bookerId).getId();
        bookingService.approveBooking(currentId, ownerId, true);

        BookingCreateDto futureDto = new BookingCreateDto(itemId, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        Long futureId = bookingService.createBooking(futureDto, bookerId).getId();

        BookingCreateDto rejectedDto = new BookingCreateDto(itemId, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4));
        Long rejectedId = bookingService.createBooking(rejectedDto, bookerId).getId();
        bookingService.approveBooking(rejectedId, ownerId, false);

        assertFalse(bookingService.getBookingsByBooker(bookerId, "ALL").isEmpty(), "ALL failed");
        assertFalse(bookingService.getBookingsByBooker(bookerId, "PAST").isEmpty(), "PAST failed");
        assertFalse(bookingService.getBookingsByBooker(bookerId, "CURRENT").isEmpty(), "CURRENT failed");
        assertFalse(bookingService.getBookingsByBooker(bookerId, "FUTURE").isEmpty(), "FUTURE failed");
        assertFalse(bookingService.getBookingsByBooker(bookerId, "WAITING").isEmpty(), "WAITING failed");
        assertFalse(bookingService.getBookingsByBooker(bookerId, "REJECTED").isEmpty(), "REJECTED failed");

        assertFalse(bookingService.getBookingsByOwner(ownerId, "ALL").isEmpty(), "Owner ALL failed");
        assertFalse(bookingService.getBookingsByOwner(ownerId, "PAST").isEmpty(), "Owner PAST failed");
        assertFalse(bookingService.getBookingsByOwner(ownerId, "CURRENT").isEmpty(), "Owner CURRENT failed");
        assertFalse(bookingService.getBookingsByOwner(ownerId, "FUTURE").isEmpty(), "Owner FUTURE failed");
        assertFalse(bookingService.getBookingsByOwner(ownerId, "WAITING").isEmpty(), "Owner WAITING failed");
        assertFalse(bookingService.getBookingsByOwner(ownerId, "REJECTED").isEmpty(), "Owner REJECTED failed");
    }
}