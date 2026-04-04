package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ItemServiceImplTest {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private CommentService commentService;

    private Long ownerId;
    private Long bookerId;
    private Long itemId;

    @BeforeEach
    void setUp() {
        ownerId = userService.create(new UserDto(null, "Owner", "owner@mail.com")).getId();
        bookerId = userService.create(new UserDto(null, "Booker", "booker@mail.com")).getId();

        ItemDto itemDto = new ItemDto(null, "Item", "Desc", true, null, null, null, null);
        itemId = itemService.create(itemDto, ownerId).getId();
    }

    @Test
    void getById_whenOwner_thenReturnsWithBookings() {
        BookingCreateDto bookingDto = new BookingCreateDto(itemId, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));
        Long bookingId = bookingService.createBooking(bookingDto, bookerId).getId();
        bookingService.approveBooking(bookingId, ownerId, true);

        commentService.createComment(new CommentDto(null, "Great!", null, null), itemId, bookerId);

        ItemDto foundItem = itemService.getById(itemId, ownerId);

        assertNotNull(foundItem.getLastBooking());
        assertEquals(1, foundItem.getComments().size());
        assertEquals("Great!", foundItem.getComments().getFirst().getText());
    }

    @Test
    void getById_whenNotOwner_thenReturnsWithoutBookings() {
        ItemDto foundItem = itemService.getById(itemId, bookerId);

        assertNull(foundItem.getLastBooking());
        assertNull(foundItem.getNextBooking());
    }

    @Test
    void searchAvailableItems_whenValid_thenReturnMatch() {
        List<ItemDto> result = itemService.searchAvailableItems("desc");
        assertEquals(1, result.size());
        assertEquals(itemId, result.getFirst().getId());
    }

    @Test
    void delete_whenOwner_thenItemDeleted() {
        itemService.delete(itemId, ownerId);
        assertTrue(itemService.searchAvailableItems("desc").isEmpty());
    }
}