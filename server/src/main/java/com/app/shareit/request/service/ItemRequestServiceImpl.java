package com.app.shareit.request.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.app.shareit.exception.NotFoundException;
import com.app.shareit.item.model.Item;
import com.app.shareit.item.repository.ItemRepository;
import com.app.shareit.request.dto.*;
import com.app.shareit.request.dto.mapper.ItemRequestMapper;
import com.app.shareit.request.model.ItemRequest;
import com.app.shareit.request.repository.ItemRequestRepository;
import com.app.shareit.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserService userService;
    private final ItemRequestMapper mapper;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestFullDto createRequest(ItemRequestCreateDto dto, Long requestorId) {
        userService.validateUserExists(requestorId);
        ItemRequest request = mapper.fromDto(dto, requestorId);
        request.setCreated(LocalDateTime.now());
        ItemRequest savedRequest = repository.save(request);
        return mapper.toDto(savedRequest, List.of());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestFullDto getRequestById(Long itemRequestId) {
        ItemRequest itemRequest = repository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос id=%d не найден", itemRequestId)));
        List<RequestedItemDto> requestedItemDtoList = itemRepository.findByItemRequestIdIn(List.of(itemRequestId))
                .stream().map(
                        item -> new RequestedItemDto(item.getId(), item.getName(), item.getOwner().getId())
                ).toList();
        return mapper.toDto(itemRequest, requestedItemDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestFullDto> getRequestsByRequestorId(Long requestorId) {
        userService.validateUserExists(requestorId);
        List<ItemRequest> requests = repository.findAllByRequestorIdOrderByCreatedDesc(requestorId);
        if (requests.isEmpty()) {
            return List.of();
        }
        List<Long> requestIdList = requests.stream().mapToLong(ItemRequest::getId).boxed().toList();
        List<Item> itemOnRequestList = itemRepository.findByItemRequestIdIn(requestIdList);
        Map<Long, List<RequestedItemDto>> requestedItemDtoMap = itemOnRequestList.stream()
                .collect(Collectors.groupingBy(
                                itemId -> itemId.getItemRequest().getId(),
                                Collectors.mapping(
                                        item -> new RequestedItemDto(item.getId(), item.getName(), item.getOwner().getId()),
                                        Collectors.toList()
                                )
                        )
                );
        return requests.stream()
                .map(itemRequest -> mapper.toDto(itemRequest,
                        requestedItemDtoMap.getOrDefault(itemRequest.getId(), List.of()))
                )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestLightDto> getAllRequests(Long userId) {
        return mapper.toDtoList(repository.findAllByRequestorIdNotOrderByCreatedDesc(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public void validateRequestId(Long itemRequestId) {
        if (itemRequestId == null) {
            return;
        }
        if (!repository.existsById(itemRequestId)) {
            throw new NotFoundException(String.format("Запрос id=%d на создание вещи не найден", itemRequestId));
        }
    }
}
