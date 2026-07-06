package com.app.shareit.extra;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.app.shareit.booking.service.BookingService;
import com.app.shareit.item.dto.ItemDto;
import com.app.shareit.item.service.ItemService;
import com.app.shareit.request.service.ItemRequestService;
import com.app.shareit.user.dto.UserDto;
import com.app.shareit.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
class LineCoverageTests {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemRequestService requestService;
    @Autowired
    private BookingService bookingService;

    private Long userWithNoItemsId;

    @BeforeEach
    void setUp() {
        userWithNoItemsId = userService.create(new UserDto(null, "NoItemsUser", "noitems@mail.com")).getId();
    }

    @Test
    void testEmptyCases() {
        List<ItemDto> items = itemService.getAllByOwnerId(userWithNoItemsId);
        assertTrue(items.isEmpty());

        List<?> requests = requestService.getRequestsByRequestorId(userWithNoItemsId);
        assertTrue(requests.isEmpty());

        assertTrue(bookingService.getBookingsByBooker(userWithNoItemsId, "ALL").isEmpty());
        assertTrue(bookingService.getBookingsByOwner(userWithNoItemsId, "ALL").isEmpty());
    }
}