package ru.practicum.shareit.request.controller;

import java.util.List;

import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.request.dto.*;

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
    public ItemRequestFullDto createRequest(@Validated @RequestBody ItemRequestCreateDto dto,
                                            @Positive @RequestHeader(value = userIdHeader) Long requestorId) {
        return restClient
                .post()
                .header(userIdHeader, String.valueOf(requestorId))
                .body(dto)
                .retrieve()
                .body(ItemRequestFullDto.class);
    }

    @GetMapping
    public List<ItemRequestFullDto> getRequestsByRequestorId(
            @Positive @RequestHeader(value = userIdHeader) Long requestorId) {
        return restClient
                .get()
                .header(userIdHeader, String.valueOf(requestorId))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @GetMapping("/all")
    public List<ItemRequestLightDto> getAllRequests(@Positive @RequestHeader(userIdHeader) Long userId) {
        return restClient
                .get()
                .uri("/all")
                .header(userIdHeader, String.valueOf(userId))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    @GetMapping("/{requestId}")
    public ItemRequestFullDto getRequestById(@PathVariable Long requestId,
                                             @Positive @RequestHeader(value = userIdHeader) Long requestorId) {
        return restClient
                .get()
                .uri("/{requestId}", requestId)
                .header(userIdHeader, String.valueOf(requestorId))
                .retrieve()
                .body(ItemRequestFullDto.class);
    }
}
