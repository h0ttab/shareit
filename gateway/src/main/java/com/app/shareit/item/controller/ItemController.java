package com.app.shareit.item.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.app.shareit.item.client.ItemClient;
import com.app.shareit.item.dto.CommentDto;
import com.app.shareit.item.dto.ItemDto;
import com.app.shareit.item.dto.ItemDto.Create;
import com.app.shareit.item.dto.ItemDto.Update;
import static com.app.shareit.util.Constants.USER_ID_HEADER;

@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "Items", description = "Items and comments management API")
public class ItemController {
    private final ItemClient client;

    @Operation(summary = "Get all items owned by current user")
    @GetMapping
    public List<ItemDto> getAllItemsByOwner(@Positive @RequestHeader(value = USER_ID_HEADER) Long ownerId) {
        return client.getAllItemsByOwner(ownerId);
    }

    @Operation(summary = "Get item by ID")
    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId,
                               @Positive @RequestHeader(value = USER_ID_HEADER) Long userId) {
        return client.getItemById(itemId, userId);
    }

    @Operation(summary = "Search available items by text")
    @GetMapping("/search")
    public List<ItemDto> searchAvailableItems(@RequestParam("text") String query) {
        if (query.isBlank()) return List.of();
        return client.searchAvailableItems(query);
    }

    @Operation(summary = "Add new item")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@Positive @RequestHeader(value = USER_ID_HEADER) Long ownerId,
                              @Validated(Create.class) @RequestBody ItemDto itemDto) {
        return client.createItem(ownerId, itemDto);
    }

    @Operation(summary = "Add comment to item")
    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestHeader(value = USER_ID_HEADER) Long userId,
                                    @PathVariable Long itemId,
                                    @Validated @RequestBody CommentDto commentDto) {
        return client.createComment(userId, itemId, commentDto);
    }

    @Operation(summary = "Update item information")
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Positive @RequestHeader(value = USER_ID_HEADER) Long ownerId,
                              @PathVariable Long itemId,
                              @Validated(Update.class) @RequestBody ItemDto itemDto) {
        return client.updateItem(ownerId, itemId, itemDto);
    }

    @Operation(summary = "Delete item")
    @DeleteMapping("/{itemId}")
    public void deleteItem(@Positive @RequestHeader(value = USER_ID_HEADER) Long ownerId,
                           @PathVariable("itemId") Long itemId) {
        client.deleteItem(ownerId, itemId);
    }
}