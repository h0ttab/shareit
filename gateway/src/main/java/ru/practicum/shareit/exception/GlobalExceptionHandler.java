package ru.practicum.shareit.exception;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ErrorResponse> handleServerException(ServerException e) {
        log.error(e.getBody());
        return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(e.getStatus(), e.getBody()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        Stream<String> fieldErrorList = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage);
        Stream<String> globalErrorList = e.getBindingResult().getGlobalErrors().stream()
                .map(ObjectError::getDefaultMessage);

        return new ErrorResponse(400, Stream.concat(globalErrorList, fieldErrorList)
                .collect(Collectors.joining(", ")));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return new ErrorResponse(400, e.getMessage());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingHeader(MissingRequestHeaderException e) {
        log.error(e.getMessage());
        return new ErrorResponse(400, e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(Throwable e) {
        log.error("Непредвиденная ошибка сервера: {}", e.getMessage(), e);
        return new ErrorResponse(500, "Внутренняя ошибка сервера");
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ErrorResponse(int statusCode, String error) {
        public static ErrorResponse readFromClientResponse(ClientHttpResponse res) throws IOException {
            try {
                return new ObjectMapper().readValue(res.getBody().readAllBytes(), ErrorResponse.class);
            } catch (IOException e) {
                return new ErrorResponse(res.getStatusCode().value(), "Неизвестная ошибка сервера");
            }
        }
    }
}
