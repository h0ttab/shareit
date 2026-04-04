package ru.practicum.shareit.item.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validateCreate_whenValid_thenNoViolations() {
        ItemDto dto = new ItemDto(null, "Дрель", "Простая дрель", true, null, null, null, null);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto, ItemDto.Create.class);
        assertThat(violations).isEmpty();
    }

    @Test
    void validateCreate_whenNameIsBlank_thenViolation() {
        ItemDto dto = new ItemDto(null, "   ", "Простая дрель", true, null, null, null, null);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto, ItemDto.Create.class);
        assertThat(violations).isNotEmpty();
    }

    @Test
    void validateCreate_whenAvailableIsNull_thenViolation() {
        ItemDto dto = new ItemDto(null, "Дрель", "Простая дрель", null, null, null, null, null);
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto, ItemDto.Create.class);
        assertThat(violations).isNotEmpty();
    }

    @Test
    void validateUpdate_whenPartialData_thenNoViolations() {
        ItemDto dto = new ItemDto();
        dto.setName("Новое имя");
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(dto, ItemDto.Update.class);
        assertThat(violations).isEmpty();
    }
}