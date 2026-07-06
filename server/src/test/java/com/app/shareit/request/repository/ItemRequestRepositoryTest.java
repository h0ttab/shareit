package com.app.shareit.request.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.app.shareit.request.model.ItemRequest;
import com.app.shareit.user.model.User;
import com.app.shareit.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllByRequestorIdNotOrderByCreatedDesc_whenValid_thenReturnList() {
        User user1 = new User();
        user1.setName("User1");
        user1.setEmail("u1@mail.com");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("User2");
        user2.setEmail("u2@mail.com");
        userRepository.save(user2);

        ItemRequest request = new ItemRequest();
        request.setDescription("Need item");
        request.setRequestor(user2);
        request.setCreated(LocalDateTime.now());
        requestRepository.save(request);

        List<ItemRequest> result = requestRepository.findAllByRequestorIdNotOrderByCreatedDesc(user1.getId());

        assertEquals(1, result.size());
        assertEquals(user2.getId(), result.getFirst().getRequestor().getId());
    }
}