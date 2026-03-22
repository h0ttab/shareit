package ru.practicum.shareit.item.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

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

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(value = userIdHeader) Long userId,
                                    @PathVariable Long itemId,
                                    @RequestBody CommentDto commentDto) {
        return commentService.createComment(commentDto, itemId, userId);
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
