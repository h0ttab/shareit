package com.app.shareit.item.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.app.shareit.item.model.Comment;
import com.app.shareit.item.model.Item;
import com.app.shareit.user.model.User;
import com.app.shareit.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByItemIdIn_whenValid_thenReturnList() {
        User user = new User();
        user.setName("User");
        user.setEmail("u@mail.com");
        userRepository.save(user);

        Item item = new Item();
        item.setName("Item");
        item.setDescription("Desc");
        item.setAvailable(true);
        item.setOwner(user);
        itemRepository.save(item);

        Comment comment = new Comment();
        comment.setText("Great!");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);

        List<Comment> result = commentRepository.findByItemIdIn(List.of(item.getId()));

        assertEquals(1, result.size());
        assertEquals("Great!", result.getFirst().getText());
    }
}