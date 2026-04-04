package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestFullDto;
import ru.practicum.shareit.request.dto.ItemRequestLightDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestService requestService;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

    private Long requestorId;
    private Long otherUserId;

    @BeforeEach
    void setUp() {
        UserDto requestor = userService.create(new UserDto(null, "Requestor", "req@mail.com"));
        requestorId = requestor.getId();

        UserDto otherUser = userService.create(new UserDto(null, "Other", "other@mail.com"));
        otherUserId = otherUser.getId();
    }

    @Test
    void createAndGetRequest_whenValid_thenSuccess() {
        ItemRequestCreateDto createDto = new ItemRequestCreateDto("Need a drill");
        ItemRequestFullDto created = requestService.createRequest(createDto, requestorId);

        assertNotNull(created.getId());
        assertEquals("Need a drill", created.getDescription());

        // Добавляем ответ на запрос от другого пользователя
        ItemDto itemDto = new ItemDto(null, "Drill", "Powerful", true, null,
                null, null, created.getId());
        itemService.create(itemDto, otherUserId);

        // Проверяем получение своего списка запросов вместе с ответами (items)
        List<ItemRequestFullDto> myRequests = requestService.getRequestsByRequestorId(requestorId);
        assertEquals(1, myRequests.size());
        assertEquals(1, myRequests.getFirst().getItems().size());
        assertEquals("Drill", myRequests.getFirst().getItems().getFirst().getName());
    }

    @Test
    void getAllRequests_whenValid_thenReturnRequestsFromOtherUsers() {
        ItemRequestCreateDto createDto = new ItemRequestCreateDto("Need a drill");
        requestService.createRequest(createDto, requestorId);

        // otherUserId должен видеть запрос от requestorId
        List<ItemRequestLightDto> allRequests = requestService.getAllRequests(otherUserId);
        assertEquals(1, allRequests.size());

        // requestorId не должен видеть свой же запрос в общем поиске
        List<ItemRequestLightDto> myAllRequests = requestService.getAllRequests(requestorId);
        assertTrue(myAllRequests.isEmpty());
    }
}