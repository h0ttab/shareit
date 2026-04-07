package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookingTest {

    @Test
    void testBookingEqualsAndHashCode() {
        Booking booking1 = new Booking();
        booking1.setId(1L);

        Booking booking2 = new Booking();
        booking2.setId(1L);

        Booking booking3 = new Booking();
        booking3.setId(2L);

        assertThat(booking1).isEqualTo(booking2);
        assertThat(booking1).isNotEqualTo(booking3);
        assertThat(booking1.hashCode()).isEqualTo(booking2.hashCode());
    }
}