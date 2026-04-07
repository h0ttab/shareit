package ru.practicum.shareit.item.controller;

import java.util.List;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDto.Create;
import ru.practicum.shareit.item.dto.ItemDto.Update;

import static ru.practicum.shareit.util.Constants.USER_ID_HEADER;

@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemController {
    private final ItemClient client;

    @GetMapping
    public List<ItemDto> getAllItemsByOwner(@Positive @RequestHeader(value = USER_ID_HEADER) Long ownerId) {
        return client.getAllItemsByOwner(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId,
                               @Positive @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return client.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAvailableItems(@RequestParam("text") String query) {
        if (query.isBlank()) return List.of();
        return client.searchAvailableItems(query);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@Positive @RequestHeader(value = USER_ID_HEADER) Long ownerId,
                              @Validated(Create.class) @RequestBody ItemDto itemDto) {
        return client.createItem(ownerId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestHeader(value = USER_ID_HEADER) Long userId,
                                    @PathVariable Long itemId,
                                    @Validated @RequestBody CommentDto commentDto) {
        return client.createComment(userId, itemId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Positive @RequestHeader(value = USER_ID_HEADER) Long ownerId,
                              @PathVariable Long itemId,
                              @Validated(Update.class) @RequestBody ItemDto itemDto) {
        return client.updateItem(ownerId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@Positive @RequestHeader(value = USER_ID_HEADER) Long ownerId,
                           @PathVariable("itemId") Long itemId) {
        client.deleteItem(ownerId, itemId);
    }
}
