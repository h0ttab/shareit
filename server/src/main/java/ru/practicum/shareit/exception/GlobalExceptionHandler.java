package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateData(Exception e) {
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
        return new ErrorResponse(404, e.getMessage());
    }

    @ExceptionHandler(ItemUnavailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleItemUnavailableException(ItemUnavailableException e) {
        return new ErrorResponse(400, e.getMessage());
    }

    @ExceptionHandler(OwnerMismatchException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleOwnerMismatchException(OwnerMismatchException e) {
        return new ErrorResponse(403, e.getMessage());
    }

    public record ErrorResponse(int statusCode, String errorMessage) {
    }
}
