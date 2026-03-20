package ru.practicum.shareit.util.validation;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OwnerMismatchException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

@RequiredArgsConstructor
public class Validations {
    private final UserService userService;
    private final ItemService itemService;

    public void validateItemOwnership(Long itemId, Long ownerId) {
        validateUserExists(ownerId);
        if (!itemService.getOwnerIdByItemId(itemId).equals(ownerId)) {
            throw new OwnerMismatchException(
                    String.format("Пользователь id=%d не является владельцем вещи id=%d", ownerId, itemId)
            );
        }
    }

    public void validateUserExists(Long userId) {
        if (!userService.existsById(userId)) {
            throw new NotFoundException(String.format("Пользователь с id=%d не найден", userId));
        }
    }
}
