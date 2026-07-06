package com.app.shareit.request.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.app.shareit.request.client.ItemRequestClient;
import com.app.shareit.request.dto.*;
import static com.app.shareit.util.Constants.USER_ID_HEADER;

@Validated
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "Item Requests", description = "Item requests view and creation API")
public class ItemRequestController {
    private final ItemRequestClient client;

    @Operation(summary = "Create item request")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestFullDto createRequest(@Validated @RequestBody ItemRequestCreateDto dto,
                                            @Positive @RequestHeader(value = USER_ID_HEADER) Long requestorId) {
        return client.createRequest(dto, requestorId);
    }

    @Operation(summary = "Get user's requests and their statuses")
    @GetMapping
    public List<ItemRequestFullDto> getRequestsByRequestorId(
            @Positive @RequestHeader(value = USER_ID_HEADER) Long requestorId) {
        return client.getRequestsByRequestorId(requestorId);
    }

    @Operation(summary = "Get other users' requests")
    @GetMapping("/all")
    public List<ItemRequestLightDto> getAllRequests(@Positive @RequestHeader(USER_ID_HEADER) Long userId) {
        return client.getAllRequests(userId);
    }

    @Operation(summary = "Get specific request by its ID")
    @GetMapping("/{requestId}")
    public ItemRequestFullDto getRequestById(@PathVariable Long requestId,
                                             @Positive @RequestHeader(value = USER_ID_HEADER) Long requestorId) {
        return client.getRequestById(requestId, requestorId);
    }
}