package ru.practicum.shareit.item.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemController {
    private final ItemService itemService;
    private final String userIdHeader = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDto> getAllItemsByOwner(@RequestHeader(value = userIdHeader) Long ownerId) {
        return itemService.getAllByOwnerId(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        return itemService.getById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAvailableItems(@RequestParam("text") String query) {
        return itemService.searchAvailableItems(query);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(value = userIdHeader) Long userId, @RequestBody ItemDto itemDto) {
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(value = userIdHeader) Long ownerId,
                              @PathVariable("itemId") Long itemId,
                              @RequestBody ItemDto itemDto) {
        return itemService.update(itemId, itemDto, ownerId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(value = userIdHeader) Long ownerId,
                           @PathVariable("itemId") Long itemId) {
        itemService.delete(itemId, ownerId);
    }
}
