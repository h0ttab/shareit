package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    void createAndGetById_whenValid_thenUserCreatedAndFound() {
        UserDto userDto = new UserDto(null, "Ivan", "ivan@mail.com");
        UserDto createdUser = userService.create(userDto);

        assertNotNull(createdUser.getId());
        assertEquals("Ivan", createdUser.getName());

        UserDto foundUser = userService.getById(createdUser.getId());
        assertEquals(createdUser.getId(), foundUser.getId());
        assertEquals("ivan@mail.com", foundUser.getEmail());
    }

    @Test
    void update_whenValid_thenUserUpdated() {
        UserDto userDto = new UserDto(null, "Ivan", "ivan@mail.com");
        UserDto createdUser = userService.create(userDto);

        UserDto updateDto = new UserDto(null, "Ivan Updated", null);
        UserDto updatedUser = userService.update(createdUser.getId(), updateDto);

        assertEquals("Ivan Updated", updatedUser.getName());
        assertEquals("ivan@mail.com", updatedUser.getEmail());
    }

    @Test
    void delete_whenValid_thenUserDeletedAndThrowsNotFound() {
        UserDto userDto = new UserDto(null, "Ivan", "ivan@mail.com");
        UserDto createdUser = userService.create(userDto);

        userService.delete(createdUser.getId());

        assertThrows(NotFoundException.class, () -> userService.getById(createdUser.getId()));
    }
}