package ru.practicum.shareit.exception;

import java.io.IOException;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServerException.class)
    public ErrorResponse handleServerException(ServerException e) {
        log.error(e.getBody());
        return new ErrorResponse(e.getStatus(), e.getBody());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return new ErrorResponse(400, e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", ")));
    }

    public record ErrorResponse(int statusCode, String errorMessage) {
        public static ErrorResponse readFromClientResponse(ClientHttpResponse res) throws IOException {
            return new ObjectMapper().readValue(res.getBody().readAllBytes(), ErrorResponse.class);
        }
    }
}
