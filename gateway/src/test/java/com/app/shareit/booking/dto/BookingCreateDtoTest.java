package com.app.shareit.booking.dto;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingCreateDtoTest {

    @Autowired
    private JacksonTester<BookingCreateDto> json;

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testSerialize() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1).withNano(0).withSecond(15);
        LocalDateTime end = start.plusDays(2);
        BookingCreateDto dto = new BookingCreateDto(1L, start, end);

        JsonContent<BookingCreateDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(start.toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(end.toString());
    }

    @Test
    void validate_whenStartBeforeEnd_thenNoViolations() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusDays(2);
        BookingCreateDto dto = new BookingCreateDto(1L, start, end);

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void validate_whenStartAfterEnd_thenViolation() {
        LocalDateTime start = LocalDateTime.now().plusDays(3);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        BookingCreateDto dto = new BookingCreateDto(1L, start, end);

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("Дата начала бронирования должна быть раньше"));
    }

    @Test
    void validate_whenStartEqualsEnd_thenViolation() {
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        BookingCreateDto dto = new BookingCreateDto(1L, time, time);

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
    }

    @Test
    void validate_whenStartInPast_thenViolation() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        BookingCreateDto dto = new BookingCreateDto(1L, start, end);

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("не может быть в прошлом"));
    }
}