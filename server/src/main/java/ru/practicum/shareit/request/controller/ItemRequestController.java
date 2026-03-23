package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestReturnDto;
import ru.practicum.shareit.request.service.ItemRequestService;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestController {
    private final String userIdHeader = "X-Sharer-User-Id";
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestReturnDto createRequest(@RequestBody ItemRequestCreateDto dto,
                                              @RequestHeader(value = userIdHeader) Long requestorId) {
        return service.createRequest(dto, requestorId);
    }
}
