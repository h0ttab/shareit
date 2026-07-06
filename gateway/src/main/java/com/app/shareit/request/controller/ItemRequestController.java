package com.app.shareit.request.controller;

import java.util.List;

import com.app.shareit.request.client.ItemRequestClient;
import com.app.shareit.request.dto.*;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.app.shareit.util.Constants.USER_ID_HEADER;

@Validated
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestFullDto createRequest(@Validated @RequestBody ItemRequestCreateDto dto,
                                            @Positive @RequestHeader(value = USER_ID_HEADER) Long requestorId) {
        return client.createRequest(dto, requestorId);
    }

    @GetMapping
    public List<ItemRequestFullDto> getRequestsByRequestorId(
            @Positive @RequestHeader(value = USER_ID_HEADER) Long requestorId) {
        return client.getRequestsByRequestorId(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestLightDto> getAllRequests(@Positive @RequestHeader(USER_ID_HEADER) Long userId) {
        return client.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestFullDto getRequestById(@PathVariable Long requestId,
                                             @Positive @RequestHeader(value = USER_ID_HEADER) Long requestorId) {
        return client.getRequestById(requestId, requestorId);
    }
}
