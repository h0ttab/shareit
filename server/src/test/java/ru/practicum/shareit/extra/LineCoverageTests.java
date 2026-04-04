package ru.practicum.shareit.extra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

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