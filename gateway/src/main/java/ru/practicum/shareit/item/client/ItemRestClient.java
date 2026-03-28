package ru.practicum.shareit.item.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import static ru.practicum.shareit.util.Constants.userIdHeader;

@Component
@Primary
public class ItemRestClient implements ItemClient {
    private final RestClient restClient;

    public ItemRestClient(@Autowired @Qualifier("ItemClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(Long ownerId) {
        return restClient
                .get()
                .header(userIdHeader, String.valueOf(ownerId))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        return restClient.get()
                .uri("/{itemId}", itemId)
                .header(userIdHeader, String.valueOf(userId))
                .retrieve()
                .body(ItemDto.class);
    }

    @Override
    public List<ItemDto> searchAvailableItems(String query) {
        return restClient.get()
                .uri("/search?text={query}", query)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public ItemDto createItem(Long ownerId, ItemDto itemDto) {
        return restClient
                .post()
                .header(userIdHeader, String.valueOf(ownerId))
                .body(itemDto)
                .retrieve()
                .body(ItemDto.class);
    }

    @Override
    public CommentDto createComment(Long userId, Long itemId, CommentDto commentDto) {
        return restClient.post()
                .uri("/{itemId}/comment", itemId)
                .header(userIdHeader, String.valueOf(userId))
                .body(commentDto)
                .retrieve()
                .body(CommentDto.class);
    }

    @Override
    public ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDto) {
        return restClient
                .patch()
                .uri("/{itemId}", itemId)
                .header(userIdHeader, String.valueOf(ownerId))
                .body(itemDto)
                .retrieve()
                .body(ItemDto.class);
    }

    @Override
    public void deleteItem(Long ownerId, Long itemId) {
        restClient
                .delete()
                .uri("/{itemId}", itemId)
                .header(userIdHeader, String.valueOf(ownerId))
                .retrieve();
    }
}
