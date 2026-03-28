package ru.practicum.shareit.request.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.request.dto.*;

import static ru.practicum.shareit.util.Constants.userIdHeader;

@Primary
@Component
public class ItemRequestRestClient implements ItemRequestClient {
    private final RestClient restClient;

    public ItemRequestRestClient(@Autowired @Qualifier("RequestsClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public ItemRequestFullDto createRequest(ItemRequestCreateDto dto, Long requestorId) {
        return restClient
                .post()
                .header(userIdHeader, String.valueOf(requestorId))
                .body(dto)
                .retrieve()
                .body(ItemRequestFullDto.class);
    }

    @Override
    public List<ItemRequestFullDto> getRequestsByRequestorId(Long requestorId) {
        return restClient
                .get()
                .header(userIdHeader, String.valueOf(requestorId))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public List<ItemRequestLightDto> getAllRequests(Long userId) {
        return restClient
                .get()
                .uri("/all")
                .header(userIdHeader, String.valueOf(userId))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public ItemRequestFullDto getRequestById(Long requestId, Long requestorId) {
        return restClient
                .get()
                .uri("/{requestId}", requestId)
                .header(userIdHeader, String.valueOf(requestorId))
                .retrieve()
                .body(ItemRequestFullDto.class);
    }
}
