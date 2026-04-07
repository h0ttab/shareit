package ru.practicum.shareit.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, BookingCreateDto> {
    @Override
    public boolean isValid(BookingCreateDto dto, ConstraintValidatorContext constraintValidatorContext) {
        if (dto == null || dto.getStart() == null || dto.getEnd() == null) {
            return true;
        }

        return dto.getStart().isBefore(dto.getEnd());
    }
}
