package com.app.shareit.request.service;

import java.util.List;

import com.app.shareit.request.dto.*;

public interface ItemRequestService {
    ItemRequestFullDto createRequest(ItemRequestCreateDto dto, Long requestorId);

    ItemRequestFullDto getRequestById(Long itemRequestId);

    List<ItemRequestFullDto> getRequestsByRequestorId(Long requestorId);

    List<ItemRequestLightDto> getAllRequests(Long userId);

    void validateRequestId(Long itemRequestId);
}
