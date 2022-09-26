package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemServiceImplTest {
    private ItemService itemService;
    private ItemRepository itemRepository;
    private CommentRepository commentRepository;

    private final User user1 = new User(1L, "user1", "user1@email.com");
    private final User user2 = new User(2L, "user2", "user2@email.com");
    private final ItemRequest request = new ItemRequest(1L, "description1", user1, LocalDateTime.now());
    private final Item item1 = new Item(1L, "item1", "text1", true, user1, request);
    private final Comment comment = new Comment(1L, item1, user1, "text", LocalDateTime.now());
    private final Item item2 = new Item(2L, "item2", "text2", true, user2, request);
    private final Item item3 = new Item(3L, "TEXT1", "test", true, user1, null);

    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        commentRepository = mock(CommentRepository.class);
        UserService userService = mock(UserService.class);
        itemService = new ItemServiceImpl(itemRepository, commentRepository, userService);
    }

    @Test
    void getItemsByUserId() {
        when(itemRepository.getAllByOwnerIdOrderById(anyLong(), any())).thenReturn(List.of(item1, item3));
        assertNotNull(itemService.getItemsByUserId(0, 5, user1.getId()));
        assertEquals(2, itemService.getItemsByUserId(0, 5, user2.getId()).size());
        assertEquals(itemService.getItemsByUserId(0, 5, user2.getId()), List.of(item1, item3));
    }

    @Test
    void getAllByRequestId() {
        when(itemRepository.getAllByRequestId(anyLong())).thenReturn(List.of(item1, item2));
        assertNotNull(itemService.getAllByRequestId(request.getId()));
        assertEquals(2, itemService.getAllByRequestId(request.getId()).size());
        assertEquals(itemService.getAllByRequestId(user2.getId()), List.of(item1, item2));
    }

    @Test
    void save() {
        when(itemRepository.save(any())).thenReturn(item1);
        assertNotNull(itemService.save(item1));
        assertEquals(itemService.save(item1), item1);
    }

    @Test
    void saveComment() {
        when(commentRepository.save(any())).thenReturn(comment);
        assertNotNull(itemService.saveComment(comment, user1.getId()));
        assertEquals(itemService.saveComment(comment, user1.getId()), comment);
    }

    @Test
    void update() {
        when(itemRepository.save(any())).thenReturn(item1);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));
        assertNotNull(itemService.update(1L, item1.getId(), item1));
        assertEquals(itemService.update(1L, item1.getId(), item1), item1);
    }

    @Test
    void updateWithWrongId() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.update(item1.getId(), user1.getId(), item1));
    }

    @Test
    void findById() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));
        assertNotNull(itemService.findById(item1.getId()));
        assertEquals(itemService.findById(item1.getId()), item1);
    }

    @Test
    void findByIdWithWrongId() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.findById(item1.getId()));
    }

    @Test
    void findCommentsById() {
        when(commentRepository.findByItemId(anyLong())).thenReturn(List.of(comment));
        assertNotNull(itemService.findCommentsById(comment.getId()));
        assertEquals(1, itemService.findCommentsById(comment.getId()).size());
        assertEquals(itemService.findCommentsById(item1.getId()), List.of(comment));
    }

    @Test
    void search() {
        when(itemRepository.getAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                anyString(), anyString(), any())).thenReturn(List.of(item1, item3));
        assertNotNull(itemService.search(0, 5, "text1"));
        assertEquals(2, itemService.search(0, 5, "text1").size());
        assertEquals(itemService.search(0, 5, "text1"), List.of(item1, item3));
    }

    @Test
    void delete() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item1));
        assertNotNull(itemService.findById(item1.getId()));
        assertEquals(itemService.findById(item1.getId()), item1);
    }

    @Test
    void deleteWithWrongId() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.delete(user1.getId(), item1.getId()));
    }
}