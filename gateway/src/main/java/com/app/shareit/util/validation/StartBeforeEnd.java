package com.app.shareit.util.validation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartBeforeEndValidator.class)
public @interface StartBeforeEnd {
    String message() default "Дата начала бронирования должна быть раньше даты окончания, но не раньше текущей даты и времени.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
