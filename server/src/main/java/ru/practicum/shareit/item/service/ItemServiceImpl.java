package ru.practicum.shareit.item.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.validation.Validations;

@Primary
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final Validations validations;

    public ItemServiceImpl(
            @Autowired ItemRepository itemRepository,
            @Autowired ItemMapper itemMapper,
            @Autowired UserService userService) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        validations = new Validations(userService, this);
    }

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        validations.validateUserExists(userId);
        Item newItem = itemRepository.save(itemMapper.fromItemDto(itemDto, userId));
        return itemMapper.toItemDto(newItem);
    }

    @Override
    public ItemDto getById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        return itemMapper.toItemDto(item);
    }

    @Override
    public Long getOwnerIdByItemId(Long itemId) {
        return itemRepository.findOwnerIdByItemId(itemId);
    }

    @Override
    public List<ItemDto> getAllByOwnerId(Long ownerId) {
        validations.validateUserExists(ownerId);
        List<Item> itemList = itemRepository.findByOwnerId(ownerId);
        return itemMapper.toItemDtoList(itemList);
    }

    @Override
    public ItemDto update(Long itemId, ItemDto itemDto, Long ownerId) {
        validations.validateItemOwnership(itemId, ownerId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        Item itemUpdated = itemMapper.updateItemFromDto(itemDto, item);
        return itemMapper.toItemDto(itemUpdated);
    }

    @Override
    public List<ItemDto> searchAvailableItems(String query) {
        if (query.isBlank()) {
            return List.of();
        }
        List<Item> itemList = itemRepository.searchAvailable(query);
        return itemMapper.toItemDtoList(itemList);
    }

    @Override
    public boolean existsById(Long itemId) {
        return itemRepository.existsById(itemId);
    }

    @Override
    public void delete(Long itemId, Long ownerId) {
        validations.validateItemOwnership(itemId, ownerId);
        itemRepository.deleteById(itemId);
    }

}
