package com.app.shareit.item.client;

import java.util.List;

import com.app.shareit.item.dto.CommentDto;
import com.app.shareit.item.dto.ItemDto;

public interface ItemClient {
    List<ItemDto> getAllItemsByOwner(Long ownerId);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> searchAvailableItems(String query);

    ItemDto createItem(Long ownerId, ItemDto itemDto);

    CommentDto createComment(Long userId, Long itemId, CommentDto commentDto);

    ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDto);

    void deleteItem(Long ownerId, Long itemId);
}
