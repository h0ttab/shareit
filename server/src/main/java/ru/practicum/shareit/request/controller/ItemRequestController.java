package ru.practicum.shareit.request.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.service.ItemRequestService;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestFullDto createRequest(@RequestBody ItemRequestCreateDto dto,
                                            @RequestHeader(value = userIdHeader) Long requestorId) {
        return service.createRequest(dto, requestorId);
    }

    @GetMapping
    public List<ItemRequestFullDto> getRequestsByRequestorId(@RequestHeader(value = userIdHeader) Long requestorId) {
        return service.getRequestsByRequestorId(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestLightDto> getAllRequests(@RequestHeader(userIdHeader) Long userId) {
        return service.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestFullDto getRequestById(@PathVariable Long requestId) {
        return service.getRequestById(requestId);
    }
}
