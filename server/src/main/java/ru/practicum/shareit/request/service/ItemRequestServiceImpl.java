package ru.practicum.shareit.request.service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestReturnDto;
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

    @Override
    public ItemRequestReturnDto createRequest(ItemRequestCreateDto dto, Long requestorId) {
        userService.validateUserExists(requestorId);
        ItemRequest request = mapper.fromDto(dto, requestorId);
        request.setCreated(LocalDateTime.now());
        ItemRequest savedRequest = repository.save(request);
        return mapper.toDto(savedRequest, List.of());
    }
}
