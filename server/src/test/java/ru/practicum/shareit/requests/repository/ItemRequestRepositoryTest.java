package ru.practicum.shareit.requests.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemRequestRepository requestRepository;

    private User user1;
    private User user2;
    private ItemRequest request1;
    private ItemRequest request2;
    private ItemRequest request3;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User(1L, "user1", "user1@email.com"));
        user2 = userRepository.save(new User(2L, "user2", "user2@email.com"));
        request1 = requestRepository.save(new ItemRequest(1L, "description1", user1, LocalDateTime.now()));
        request2 = requestRepository.save(new ItemRequest(2L, "description2", user2, LocalDateTime.now()));
        request3 = requestRepository.save(new ItemRequest(3L, "description3", user1, LocalDateTime.now()));
    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        requestRepository.deleteAll();
    }

    @Test
    void getAllByRequesterIdOrderByCreatedDesc() {
        List<ItemRequest> requests = requestRepository.getAllByRequesterIdOrderByCreatedDesc(user1.getId());

        assertNotNull(requests);
        assertEquals(2, requests.size());
        assertEquals(requests.get(0), request3);
        assertEquals(requests.get(1), request1);
    }

    @Test
    void findAllByRequesterIdIsNot() {
        List<ItemRequest> requests = requestRepository.findAllByRequesterIdIsNot(user1.getId(), Pageable.unpaged());

        assertNotNull(requests);
        assertEquals(1, requests.size());
        assertEquals(requests.get(0), request2);
    }
}