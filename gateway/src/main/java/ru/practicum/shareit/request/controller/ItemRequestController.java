package ru.practicum.shareit.request.controller;

import java.util.List;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.*;

import static ru.practicum.shareit.util.Constants.userIdHeader;

@Validated
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestFullDto createRequest(@Validated @RequestBody ItemRequestCreateDto dto,
                                            @Positive @RequestHeader(value = userIdHeader) Long requestorId) {
        return client.createRequest(dto, requestorId);
    }

    @GetMapping
    public List<ItemRequestFullDto> getRequestsByRequestorId(
            @Positive @RequestHeader(value = userIdHeader) Long requestorId) {
        return client.getRequestsByRequestorId(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestLightDto> getAllRequests(@Positive @RequestHeader(userIdHeader) Long userId) {
        return client.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestFullDto getRequestById(@PathVariable Long requestId,
                                             @Positive @RequestHeader(value = userIdHeader) Long requestorId) {
        return client.getRequestById(requestId, requestorId);
    }
}
