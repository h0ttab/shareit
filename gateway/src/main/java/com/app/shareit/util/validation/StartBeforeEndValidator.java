package com.app.shareit.util.validation;

import com.app.shareit.booking.dto.BookingCreateDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, BookingCreateDto> {
    @Override
    public boolean isValid(BookingCreateDto dto, ConstraintValidatorContext constraintValidatorContext) {
        if (dto == null || dto.getStart() == null || dto.getEnd() == null) {
            return true;
        }

        return dto.getStart().isBefore(dto.getEnd());
    }
}
