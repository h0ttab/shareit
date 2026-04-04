package ru.practicum.shareit.booking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Item item;
    private User booker;

    @BeforeEach
    void setUp() {
        User owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@mail.com");
        userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@mail.com");
        userRepository.save(booker);

        item = new Item();
        item.setName("Item");
        item.setDescription("Desc");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStartDate(LocalDateTime.now().plusDays(10));
        booking.setEndDate(LocalDateTime.now().plusDays(15));
        bookingRepository.save(booking);
    }

    @Test
    void isItemAvailableDuringDates_whenOverlapping_thenReturnFalse() {
        LocalDateTime start = LocalDateTime.now().plusDays(12);
        LocalDateTime end = LocalDateTime.now().plusDays(17);

        boolean isAvailable = bookingRepository.isItemAvailableDuringDates(item.getId(), start, end);
        assertFalse(isAvailable);
    }

    @Test
    void isItemAvailableDuringDates_whenNotOverlapping_thenReturnTrue() {
        LocalDateTime start = LocalDateTime.now().plusDays(20);
        LocalDateTime end = LocalDateTime.now().plusDays(25);

        boolean isAvailable = bookingRepository.isItemAvailableDuringDates(item.getId(), start, end);
        assertTrue(isAvailable);
    }

    @Test
    void findNextBookings_whenValid_thenReturnBooking() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> nextBookings = bookingRepository.findNextBookings(List.of(item.getId()), now);

        assertEquals(1, nextBookings.size());
        assertEquals(booker.getId(), nextBookings.getFirst().getBooker().getId());
    }
}