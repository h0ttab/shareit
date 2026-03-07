package ru.practicum.shareit.item.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.exception.GlobalExceptionHandler.ErrorResponse;
import ru.practicum.shareit.exception.ServerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDto.*;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final RestClient restClient;
    private final String userIdHeader = "X-Sharer-User-Id";

    public ItemController(@Autowired @Qualifier("ItemClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @GetMapping
    public List<ItemDto> getAllItemsByOwner(@RequestHeader(value = userIdHeader) Long ownerId) {
        return restClient.get().header(userIdHeader, String.valueOf(ownerId)).retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((req, res) -> {
                            ErrorResponse errorResponse = ErrorResponse.readFromClientResponse(res);
                            throw new ServerException(errorResponse.statusCode(), errorResponse.errorMessage());
                        })
                )
                .body(new ParameterizedTypeReference<>() {});
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        return restClient.get().uri("/{itemId}", itemId).retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((req, res) -> {
                            ErrorResponse errorResponse = ErrorResponse.readFromClientResponse(res);
                            throw new ServerException(errorResponse.statusCode(), errorResponse.errorMessage());
                        })
                )
                .body(ItemDto.class);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAvailableItems(@RequestParam("text") String query) {
        return restClient.get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/search")
                                .queryParam("text", query)
                                .build()
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((req, res) -> {
                            ErrorResponse errorResponse = ErrorResponse.readFromClientResponse(res);
                            throw new ServerException(errorResponse.statusCode(), errorResponse.errorMessage());
                        })
                )
                .body(new ParameterizedTypeReference<>() {});
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(value = userIdHeader) Long ownerId,
                              @Validated(Create.class) @RequestBody ItemDto itemDto) {
        return restClient.post().header(userIdHeader, String.valueOf(ownerId)).body(itemDto).retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((req, res) -> {
                            ErrorResponse errorResponse = ErrorResponse.readFromClientResponse(res);
                            throw new ServerException(errorResponse.statusCode(), errorResponse.errorMessage());
                        })
                )
                .body(ItemDto.class);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(value = userIdHeader) Long ownerId,
                              @PathVariable("itemId") Long itemId,
                              @Validated(Update.class) @RequestBody ItemDto itemDto) {
        return restClient.patch().uri("/{itemId}", itemId).header(userIdHeader, String.valueOf(ownerId)).body(itemDto).retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((req, res) -> {
                            ErrorResponse errorResponse = ErrorResponse.readFromClientResponse(res);
                            throw new ServerException(errorResponse.statusCode(), errorResponse.errorMessage());
                        })
                )
                .body(ItemDto.class);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(value = userIdHeader) Long ownerId,
                           @PathVariable("itemId") Long itemId) {
        restClient.delete().uri("/{itemId}", itemId).header(userIdHeader, String.valueOf(ownerId))
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((req, res) -> {
                            ErrorResponse errorResponse = ErrorResponse.readFromClientResponse(res);
                            throw new ServerException(errorResponse.statusCode(), errorResponse.errorMessage());
                        })
                );
    }
}
