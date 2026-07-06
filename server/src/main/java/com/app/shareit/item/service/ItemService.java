package com.app.shareit.item.service;

import java.util.List;

import com.app.shareit.item.dto.ItemDto;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Long userId);

    ItemDto getById(Long itemId, Long userId);

    Long getOwnerIdByItemId(Long itemId);

    List<ItemDto> getAllByOwnerId(Long ownerId);

    ItemDto update(Long itemId, ItemDto itemDto, Long ownerId);

    List<ItemDto> searchAvailableItems(String query);

    void validateItemOwnership(Long itemId, Long ownerId);

    void delete(Long itemId, Long ownerId);
}
