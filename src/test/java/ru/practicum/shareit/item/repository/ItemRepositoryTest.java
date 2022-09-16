package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemRequestRepository requestRepository;

    private User user1;
    private User user2;
    private Item item1;
    private Item item2;
    private Item item3;
    private ItemRequest request;


    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User(1L, "user1", "user1@email.com"));
        user2 = userRepository.save(new User(2L, "user2", "user2@email.com"));
        request = requestRepository.save(new ItemRequest(1L, "description", user1, LocalDateTime.now()));
        item1 = itemRepository.save(new Item(1L, "item1", "text1", true, user1, request));
        item2 = itemRepository.save(new Item(2L, "item2", "text2", true, user2, request));
        item3 = itemRepository.save(new Item(3L, "TEXT2", "test", true, user1, null));
    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        requestRepository.deleteAll();
    }

    @Test
    void getAllByOwnerIdOrderById() {
        List<Item> items = itemRepository.getAllByOwnerIdOrderById(user1.getId(), Pageable.unpaged());
        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals(items.get(0), item1);
        assertEquals(items.get(1), item3);
    }

    @Test
    void getAllByRequestId() {
        List<Item> items = itemRepository.getAllByRequestId(request.getId());
        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals(items.get(0), item1);
        assertEquals(items.get(1), item2);
    }

    @Test
    void getAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue() {
        List<Item> items =
                itemRepository.getAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                        "ext2", "ext2", Pageable.unpaged());

        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals(items.get(0), item2);
        assertEquals(items.get(1), item3);
    }
}