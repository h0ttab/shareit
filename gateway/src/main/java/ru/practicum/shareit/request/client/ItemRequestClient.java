package ru.practicum.shareit.request.client;

import java.util.List;

import ru.practicum.shareit.request.dto.*;

public interface ItemRequestClient {
    ItemRequestFullDto createRequest(ItemRequestCreateDto dto, Long requestorId);

    List<ItemRequestFullDto> getRequestsByRequestorId(Long requestorId);

    List<ItemRequestLightDto> getAllRequests(Long userId);

    ItemRequestFullDto getRequestById(Long requestId, Long requestorId);
}
