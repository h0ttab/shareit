package ru.practicum.shareit.item.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OwnerMismatchException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

@Service
@Primary
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        Item newItem = itemRepository.save(itemMapper.fromItemDto(itemDto, userId));
        return itemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDto getById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllByOwnerId(Long ownerId) {
        List<Item> itemList = itemRepository.findByOwnerId(ownerId);
        return itemMapper.toItemDtoList(itemList);
    }

    @Override
    public ItemDto update(Long itemId, ItemDto itemDto, Long ownerId) {
        validateItemOwnership(itemId, ownerId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        Item itemUpdated = itemMapper.updateItemFromDto(itemDto, item);
        return itemMapper.toItemDto(itemUpdated);
    }

    @Override
    public List<ItemDto> searchAvailableItems(String query) {
        List<Item> itemList = itemRepository.searchAvailable(query);
        return itemMapper.toItemDtoList(itemList);
    }

    @Override
    public void delete(Long itemId, Long ownerId) {
        validateItemOwnership(itemId, ownerId);
    }

    @Override
    public void validateItemOwnership(Long itemId, Long ownerId) {
        if (!getById(itemId).getId().equals(ownerId)) {
            throw new OwnerMismatchException(
                    String.format("Пользователь id=%d не является владельцем вещи id=%d", ownerId, itemId)
            );
        }
    }
}
