package ru.practicum.shareit.request.service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.dto.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

@Service
@Primary
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserService userService;
    private final ItemRequestMapper mapper;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestReturnDto createRequest(ItemRequestCreateDto dto, Long requestorId) {
        userService.validateUserExists(requestorId);
        ItemRequest request = mapper.fromDto(dto, requestorId);
        request.setCreated(LocalDateTime.now());
        ItemRequest savedRequest = repository.save(request);
        return mapper.toDto(savedRequest, List.of());
    }

    @Override
    public ItemRequestReturnDto getRequestById(Long itemRequestId) {
        ItemRequest itemRequest = repository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос id=%d не найден", itemRequestId)));
        List<RequestedItemDto> requestedItemDtoList = itemRepository.findByItemRequestIdIn(List.of(itemRequestId))
                .stream().map(
                        item -> new RequestedItemDto(item.getId(), item.getName(), item.getOwner().getId())
                ).toList();
        return mapper.toDto(itemRequest, requestedItemDtoList);
    }

    @Override
    public void validateRequestId(Long itemRequestId) {
        if (itemRequestId == null) {
            return;
        }
        if (!repository.existsById(itemRequestId)) {
            throw new NotFoundException("Запрос id=%d на создание вещи не найден");
        }
    }
}
