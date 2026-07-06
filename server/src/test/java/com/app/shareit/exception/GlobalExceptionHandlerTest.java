package com.app.shareit.exception;

import org.junit.jupiter.api.Test;

import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleDataIntegrityViolation_whenNoSpecificConstraint_thenGenericMessage() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("random error");
        GlobalExceptionHandler.ErrorResponse response = handler.handleDuplicateData(ex);

        assertEquals(409, response.statusCode());
        assertEquals("Ошибка при сохранении данных: random error", response.error());
    }
}