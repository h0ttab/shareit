package ru.practicum.shareit.request.service;

import java.util.List;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestReturnDto;

public interface ItemRequestService {
    ItemRequestReturnDto createRequest(ItemRequestCreateDto dto, Long requestorId);

    ItemRequestReturnDto getRequestById(Long itemRequestId);

    List<ItemRequestReturnDto> getRequestsByRequestorId(Long requestorId);

    void validateRequestId(Long itemRequestId);
}
