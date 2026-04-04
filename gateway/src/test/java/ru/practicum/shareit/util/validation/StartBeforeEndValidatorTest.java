package ru.practicum.shareit.util.validation;

import org.junit.jupiter.api.Test;

import ru.practicum.shareit.booking.dto.BookingCreateDto;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StartBeforeEndValidatorTest {

    @Test
    void isValid_whenDtoOrDatesAreNull_thenReturnsTrue() {
        StartBeforeEndValidator validator = new StartBeforeEndValidator();

        assertTrue(validator.isValid(null, null));

        BookingCreateDto dto = new BookingCreateDto();
        assertTrue(validator.isValid(dto, null));
    }
}