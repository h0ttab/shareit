package ru.practicum.shareit.user.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testSerialize() throws Exception {
        UserDto dto = new UserDto(1L, "Ivan", "ivan@mail.com");

        JsonContent<UserDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Ivan");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("ivan@mail.com");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\": 1, \"name\": \"Ivan\", \"email\": \"ivan@mail.com\"}";

        UserDto dto = json.parseObject(content);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Ivan");
        assertThat(dto.getEmail()).isEqualTo("ivan@mail.com");
    }

    @Test
    void validateCreate_whenValid_thenNoViolations() {
        UserDto dto = new UserDto(null, "Ivan", "ivan@mail.com");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto, UserDto.Create.class);
        assertThat(violations).isEmpty();
    }

    @Test
    void validateCreate_whenNameIsBlank_thenViolation() {
        UserDto dto = new UserDto(null, "   ", "ivan@mail.com");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto, UserDto.Create.class);
        assertThat(violations).isNotEmpty();
    }

    @Test
    void validateCreate_whenEmailIsInvalid_thenViolation() {
        UserDto dto = new UserDto(null, "Ivan", "invalid-email");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto, UserDto.Create.class);
        assertThat(violations).isNotEmpty();
    }

    @Test
    void validateUpdate_whenNameAndEmailAreValid_thenNoViolations() {
        UserDto dto = new UserDto(null, "Ivan Updated", "new@mail.com");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto, UserDto.Update.class);
        assertThat(violations).isEmpty();
    }

    @Test
    void validateUpdate_whenEmailIsNull_thenNoViolationsBecauseOptional() {
        UserDto dto = new UserDto(null, "Ivan", null);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(dto, UserDto.Update.class);
        assertThat(violations).isEmpty();
    }
}