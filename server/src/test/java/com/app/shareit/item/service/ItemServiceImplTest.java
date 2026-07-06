package com.app.shareit.item.service;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.app.shareit.booking.dto.BookingCreateDto;
import com.app.shareit.booking.service.BookingService;
import com.app.shareit.item.dto.CommentDto;
import com.app.shareit.item.dto.ItemDto;
import com.app.shareit.user.dto.UserDto;
import com.app.shareit.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    void getAllByOwnerId_whenItemHasNoBookings_thenReturnsWithNulls() {
        ItemDto itemDto = new ItemDto(null, "New Item", "No Bookings", true,
                null, null, null, null);
        itemService.create(itemDto, ownerId);

        List<ItemDto> result = itemService.getAllByOwnerId(ownerId);

        ItemDto found = result.stream().filter(i -> i.getName().equals("New Item")).findFirst().orElseThrow();
        assertNull(found.getLastBooking());
        assertNull(found.getNextBooking());
    }

    @Test
    void getAllByOwnerId_whenOwnerHasNoItems_thenReturnsEmptyList() {
        List<ItemDto> result = itemService.getAllByOwnerId(bookerId);
        assertTrue(result.isEmpty());
    }

    @Test
    void getById_whenItemHasNoComments_thenReturnsWithEmptyCommentsList() {
        ItemDto foundItem = itemService.getById(itemId, ownerId);
        assertTrue(foundItem.getComments().isEmpty());
    }

    @Test
    void getCommentsByOwnerId_whenExists_thenReturnList() {
        BookingCreateDto bookingDto = new BookingCreateDto(itemId, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1));
        Long bookingId = bookingService.createBooking(bookingDto, bookerId).getId();
        bookingService.approveBooking(bookingId, ownerId, true);

        commentService.createComment(new CommentDto(null, "Great owner!", null, null), itemId, bookerId);

        List<CommentDto> comments = commentService.getCommentsByOwnerId(ownerId);

        assertFalse(comments.isEmpty());
        assertEquals("Great owner!", comments.getFirst().getText());
    }

    @Test
    void update_whenOwner_thenNameAndAvailableUpdated() {
        ItemDto updateDto = new ItemDto();
        updateDto.setName("Super Drill");
        updateDto.setAvailable(false);

        ItemDto updated = itemService.update(itemId, updateDto, ownerId);

        assertEquals("Super Drill", updated.getName());
        assertFalse(updated.getAvailable());
        assertEquals("Desc", updated.getDescription());
    }
}