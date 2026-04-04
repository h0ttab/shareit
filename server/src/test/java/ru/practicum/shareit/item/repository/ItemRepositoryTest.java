package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User owner;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@mail.com");
        userRepository.save(owner);

        item1 = new Item();
        item1.setName("Дрель");
        item1.setDescription("Мощная дрель");
        item1.setAvailable(true);
        item1.setOwner(owner);
        itemRepository.save(item1);

        item2 = new Item();
        item2.setName("Отвертка");
        item2.setDescription("Крестовая отвертка");
        item2.setAvailable(false);
        item2.setOwner(owner);
        itemRepository.save(item2);
    }

    @Test
    void searchAvailable_whenMatchNameAndAvailable_thenReturnList() {
        List<Item> result = itemRepository.searchAvailable("ДРЕЛЬ");
        assertEquals(1, result.size());
        assertEquals("Дрель", result.getFirst().getName());
    }

    @Test
    void searchAvailable_whenMatchDescriptionAndAvailable_thenReturnList() {
        List<Item> result = itemRepository.searchAvailable("МощНАЯ");
        assertEquals(1, result.size());
    }

    @Test
    void searchAvailable_whenMatchButUnavailable_thenReturnEmptyList() {
        List<Item> result = itemRepository.searchAvailable("Отвертка");
        assertTrue(result.isEmpty());
    }

    @Test
    void findOwnerIdByItemId_whenValid_thenReturnOwnerId() {
        Long ownerId = itemRepository.findOwnerIdByItemId(item1.getId());
        assertEquals(owner.getId(), ownerId);
    }
}