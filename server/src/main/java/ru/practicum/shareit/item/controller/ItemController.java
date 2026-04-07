package ru.practicum.shareit.item.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @GetMapping
    public List<ItemDto> getAllItemsByOwner(@RequestHeader(value = USER_ID_HEADER) Long ownerId) {
        return itemService.getAllByOwnerId(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId,
                               @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAvailableItems(@RequestParam("text") String query) {
        return itemService.searchAvailableItems(query);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(value = USER_ID_HEADER) Long userId, @RequestBody ItemDto itemDto) {
        return itemService.create(itemDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(value = USER_ID_HEADER) Long userId,
                                    @PathVariable Long itemId,
                                    @RequestBody CommentDto commentDto) {
        return commentService.createComment(commentDto, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(value = USER_ID_HEADER) Long ownerId,
                              @PathVariable("itemId") Long itemId,
                              @RequestBody ItemDto itemDto) {
        return itemService.update(itemId, itemDto, ownerId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(value = USER_ID_HEADER) Long ownerId,
                           @PathVariable("itemId") Long itemId) {
        itemService.delete(itemId, ownerId);
    }
}
