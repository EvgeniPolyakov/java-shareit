package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ItemRepository itemRepository;

    private Item item;
    private Comment comment2;

    @BeforeEach
    void setUp() {
        User user1 = userRepository.save(new User(1L, "user1", "user1@email.com"));
        item = itemRepository.save(new Item(2L, "item2", "text2", true, user1, null));
        comment2 = commentRepository.save(new Comment(2L, item, user1, "text", LocalDateTime.now()));
    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void findByItemId() {
        List<Comment> comments = commentRepository.findByItemId(item.getId());

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals(comments.get(0), comment2);
    }
}