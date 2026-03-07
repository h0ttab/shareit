package ru.practicum.shareit.item.service;

import java.util.List;

import ru.practicum.shareit.item.dto.ItemDto;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Long userId);

    ItemDto getById(Long itemId);

    Long getOwnerIdByItemId(Long itemId);

    List<ItemDto> getAllByOwnerId(Long ownerId);

    ItemDto update(Long itemId, ItemDto itemDto, Long ownerId);

    List<ItemDto> searchAvailableItems(String query);

    void delete(Long itemId, Long ownerId);
}
