package ru.practicum.shareit.request.controller;

import java.util.List;

import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.exception.GlobalExceptionHandler;
import ru.practicum.shareit.exception.ServerException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestReturnDto;

@Validated
@RestController
@RequestMapping("/requests")
public class ItemRequestController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final RestClient restClient;

    public ItemRequestController(@Autowired @Qualifier("RequestsClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @PostMapping
    public ItemRequestReturnDto createRequest(@Validated @RequestBody ItemRequestCreateDto dto,
                                              @Positive @RequestHeader(value = userIdHeader) Long requestorId) {
        return restClient.post().header(userIdHeader, String.valueOf(requestorId)).body(dto).retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((req, res) -> {
                            GlobalExceptionHandler.ErrorResponse errorResponse = GlobalExceptionHandler.ErrorResponse.readFromClientResponse(res);
                            throw new ServerException(errorResponse.statusCode(), errorResponse.error());
                        })
                )
                .body(ItemRequestReturnDto.class);
    }

    @GetMapping
    public List<ItemRequestReturnDto> getRequestsByRequestorId(
            @Positive @RequestHeader(value = userIdHeader) Long requestorId) {
        return restClient.get()
                .header(userIdHeader, String.valueOf(requestorId))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((req, res) -> {
                            GlobalExceptionHandler.ErrorResponse errorResponse = GlobalExceptionHandler.ErrorResponse.readFromClientResponse(res);
                            throw new ServerException(errorResponse.statusCode(), errorResponse.error());
                        })
                )
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @GetMapping("/{requestId}")
    public ItemRequestReturnDto getRequestById(@PathVariable Long requestId,
                                               @Positive @RequestHeader(value = userIdHeader) Long requestorId) {
        return restClient.get()
                .uri("/{requestId}", requestId)
                .header(userIdHeader, String.valueOf(requestorId))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((req, res) -> {
                            GlobalExceptionHandler.ErrorResponse errorResponse = GlobalExceptionHandler.ErrorResponse.readFromClientResponse(res);
                            throw new ServerException(errorResponse.statusCode(), errorResponse.error());
                        })
                )
                .body(ItemRequestReturnDto.class);
    }
}
