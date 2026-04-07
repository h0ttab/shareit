package ru.practicum.shareit.request.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.service.ItemRequestService;

import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestFullDto createRequest(@RequestBody ItemRequestCreateDto dto,
                                            @RequestHeader(value = USER_ID_HEADER) Long requestorId) {
        return service.createRequest(dto, requestorId);
    }

    @GetMapping
    public List<ItemRequestFullDto> getRequestsByRequestorId(@RequestHeader(value = USER_ID_HEADER) Long requestorId) {
        return service.getRequestsByRequestorId(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestLightDto> getAllRequests(@RequestHeader(USER_ID_HEADER) Long userId) {
        return service.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestFullDto getRequestById(@PathVariable Long requestId) {
        return service.getRequestById(requestId);
    }
}
