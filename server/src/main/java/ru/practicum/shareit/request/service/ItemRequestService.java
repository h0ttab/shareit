package ru.practicum.shareit.request.service;

import java.util.List;

import ru.practicum.shareit.request.dto.*;

public interface ItemRequestService {
    ItemRequestFullDto createRequest(ItemRequestCreateDto dto, Long requestorId);

    ItemRequestFullDto getRequestById(Long itemRequestId);

    List<ItemRequestFullDto> getRequestsByRequestorId(Long requestorId);

    List<ItemRequestLightDto> getAllRequests();

    void validateRequestId(Long itemRequestId);
}
