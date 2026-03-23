package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestReturnDto;

public interface ItemRequestService {
    ItemRequestReturnDto createRequest(ItemRequestCreateDto dto, Long requestorId);
}
