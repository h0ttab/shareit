package com.app.shareit.request.client;

import java.util.List;

import com.app.shareit.request.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import static com.app.shareit.util.Constants.USER_ID_HEADER;

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
                .header(USER_ID_HEADER, String.valueOf(requestorId))
                .body(dto)
                .retrieve()
                .body(ItemRequestFullDto.class);
    }

    @Override
    public List<ItemRequestFullDto> getRequestsByRequestorId(Long requestorId) {
        return restClient
                .get()
                .header(USER_ID_HEADER, String.valueOf(requestorId))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public List<ItemRequestLightDto> getAllRequests(Long userId) {
        return restClient
                .get()
                .uri("/all")
                .header(USER_ID_HEADER, String.valueOf(userId))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public ItemRequestFullDto getRequestById(Long requestId, Long requestorId) {
        return restClient
                .get()
                .uri("/{requestId}", requestId)
                .header(USER_ID_HEADER, String.valueOf(requestorId))
                .retrieve()
                .body(ItemRequestFullDto.class);
    }
}
