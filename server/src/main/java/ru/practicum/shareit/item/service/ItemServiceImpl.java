package ru.practicum.shareit.item.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OwnerMismatchException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@Primary
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ItemRequestService itemRequestService;

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        userService.validateUserExists(userId);
        itemRequestService.validateRequestId(itemDto.getRequestId());
        Item newItem = itemRepository.save(itemMapper.fromItemDto(itemDto, userId));
        return itemMapper.toItemDto(newItem, List.of());
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        if (Objects.equals(item.getOwner().getId(), userId)) {
            return addAttributesToItems(List.of(item)).getFirst();
        }
        List<CommentDto> commentDtoList = commentService.getCommentsByItemId(itemId);
        return itemMapper.toItemDto(item, commentDtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getOwnerIdByItemId(Long itemId) {
        return itemRepository.findOwnerIdByItemId(itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getAllByOwnerId(Long ownerId) {
        userService.validateUserExists(ownerId);

        List<Item> items = itemRepository.findByOwnerId(ownerId);
        if (items.isEmpty()) return List.of();

        return addAttributesToItems(items);
    }

    @Override
    public ItemDto update(Long itemId, ItemDto itemDto, Long ownerId) {
        validateItemOwnership(itemId, ownerId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        Item itemUpdated = itemMapper.updateItemFromDto(itemDto, item);
        return itemMapper.toItemDto(itemUpdated, List.of());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> searchAvailableItems(String query) {
        if (query.isBlank()) {
            return List.of();
        }
        List<Item> itemList = itemRepository.searchAvailable(query.toUpperCase());
        return itemMapper.toItemDtoList(itemList);
    }

    @Override
    @Transactional(readOnly = true)
    public void validateItemOwnership(Long itemId, Long ownerId) {
        userService.validateUserExists(ownerId);
        if (!getOwnerIdByItemId(itemId).equals(ownerId)) {
            throw new OwnerMismatchException(
                    String.format("Пользователь id=%d не является владельцем вещи id=%d", ownerId, itemId)
            );
        }
    }

    @Override
    public void delete(Long itemId, Long ownerId) {
        validateItemOwnership(itemId, ownerId);
        itemRepository.deleteById(itemId);
    }

    @Transactional(readOnly = true)
    private List<ItemDto> addAttributesToItems(List<Item> itemList) {
        List<Long> itemIds = itemList.stream().map(Item::getId).toList();
        LocalDateTime currentTime = LocalDateTime.now();

        Map<Long, Booking> lastBookings = bookingRepository.findLastBookings(itemIds, currentTime)
                .stream()
                .collect(Collectors.toMap(booking -> booking.getItem().getId(), booking -> booking));
        Map<Long, Booking> nextBookings = bookingRepository.findNextBookings(itemIds, currentTime)
                .stream()
                .collect(Collectors.toMap(booking -> booking.getItem().getId(), booking -> booking));

        Map<Long, List<CommentDto>> commentsMap = commentService.findByItemIdIn(itemIds)
                .stream()
                .collect(Collectors.groupingBy(
                        comment -> comment.getItem().getId(),
                        Collectors.mapping(commentMapper::toCommentDto, Collectors.toList())
                ));

        return itemList.stream()
                .map(item -> itemMapper.toItemDtoWithBookings(
                        item,
                        commentsMap.getOrDefault(item.getId(), List.of()),
                        bookingMapper.toBookingDateDto(lastBookings.getOrDefault(item.getId(), null)),
                        bookingMapper.toBookingDateDto(nextBookings.getOrDefault(item.getId(), null))
                ))
                .toList();
    }
}
