package com.app.shareit.request.service;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.app.shareit.item.dto.ItemDto;
import com.app.shareit.item.service.ItemService;
import com.app.shareit.request.dto.*;
import com.app.shareit.user.dto.UserDto;
import com.app.shareit.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

        ItemDto itemDto = new ItemDto(null, "Drill", "Powerful", true, null,
                null, null, created.getId());
        itemService.create(itemDto, otherUserId);

        List<ItemRequestFullDto> myRequests = requestService.getRequestsByRequestorId(requestorId);
        assertEquals(1, myRequests.size());
        assertEquals(1, myRequests.getFirst().getItems().size());
        assertEquals("Drill", myRequests.getFirst().getItems().getFirst().getName());
    }

    @Test
    void getAllRequests_whenValid_thenReturnRequestsFromOtherUsers() {
        ItemRequestCreateDto createDto = new ItemRequestCreateDto("Need a drill");
        requestService.createRequest(createDto, requestorId);

        List<ItemRequestLightDto> allRequests = requestService.getAllRequests(otherUserId);
        assertEquals(1, allRequests.size());

        List<ItemRequestLightDto> myAllRequests = requestService.getAllRequests(requestorId);
        assertTrue(myAllRequests.isEmpty());
    }

    @Test
    void getRequestsByRequestorId_whenRequestsExistButNoItems_thenReturnWithEmptyItemsList() {
        ItemRequestCreateDto createDto = new ItemRequestCreateDto("Need something");
        requestService.createRequest(createDto, requestorId);

        List<ItemRequestFullDto> myRequests = requestService.getRequestsByRequestorId(requestorId);

        assertEquals(1, myRequests.size());
        assertTrue(myRequests.getFirst().getItems().isEmpty());
    }

    @Test
    void getRequestById_whenExists_thenReturnFullDtoWithItems() {
        ItemRequestCreateDto createDto = new ItemRequestCreateDto("Need a hammer");
        ItemRequestFullDto request = requestService.createRequest(createDto, requestorId);

        ItemDto itemDto = new ItemDto(null, "Hammer", "Heavy", true, null, null, null, request.getId());
        itemService.create(itemDto, otherUserId);

        ItemRequestFullDto found = requestService.getRequestById(request.getId());

        assertNotNull(found);
        assertEquals(request.getId(), found.getId());
        assertEquals(1, found.getItems().size());
        assertEquals("Hammer", found.getItems().getFirst().getName());
    }
}