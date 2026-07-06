package com.app.shareit.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateData(DataIntegrityViolationException e) {
        log.error(e.getMessage());
        String errorMessage = "Ошибка при сохранении данных: " + e.getMessage();
        if (e.getMessage().contains("uq_user_email")) {
            errorMessage = "Такой email уже зарегистрирован";
        }
        return new ErrorResponse(409, errorMessage);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        log.error(e.getMessage());
        return new ErrorResponse(404, e.getMessage());
    }

    @ExceptionHandler(ItemUnavailableException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleItemUnavailableException(ItemUnavailableException e) {
        log.error(e.getMessage());
        return new ErrorResponse(400, e.getMessage());
    }

    @ExceptionHandler(OwnerMismatchException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleOwnerMismatchException(OwnerMismatchException e) {
        log.error(e.getMessage());
        return new ErrorResponse(403, e.getMessage());
    }

    @ExceptionHandler(BookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBookingException(BookingException e) {
        log.error(e.getMessage());
        return new ErrorResponse(400, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        log.error("Непредвиденная ошибка сервера: {}", e.getMessage(), e);
        return new ErrorResponse(500, "Внутренняя ошибка сервера");
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ErrorResponse(int statusCode, String error) {
    }
}
