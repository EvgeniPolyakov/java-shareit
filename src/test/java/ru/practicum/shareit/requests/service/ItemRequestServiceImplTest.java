package ru.practicum.shareit.requests.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemRequestServiceImplTest {
    private ItemRequestService requestService;
    private ItemRequestRepository requestRepository;

    private final User user1 = new User(1L, "user1", "user1@email.com");
    private final User user2 = new User(2L, "user2", "user2@email.com");
    private final ItemRequest request1 = new ItemRequest(1L, "description1", user1, LocalDateTime.now());
    private final ItemRequest request2 = new ItemRequest(3L, "description3", user1, LocalDateTime.now());

    @BeforeEach
    void setUp() {
        requestRepository = mock(ItemRequestRepository.class);
        requestService = new ItemRequestServiceImpl(requestRepository);
    }

    @Test
    void getAll() {
        when(requestRepository.findAllByRequesterIdIsNot(anyLong(), any())).thenReturn(List.of(request1, request2));
        assertNotNull(requestService.getAll(0, 5, user2.getId()));
        assertEquals(2, requestService.getAll(0, 5, user2.getId()).size());
        assertEquals(requestService.getAll(0, 5, user2.getId()), List.of(request1, request2));
    }

    @Test
    void getOwn() {
        when(requestRepository.getAllByRequesterIdOrderByCreatedDesc(anyLong())).thenReturn(List.of(request1, request2));
        assertNotNull(requestService.getOwn(user1.getId()));
        assertEquals(2, requestService.getOwn(user2.getId()).size());
        assertEquals(requestService.getOwn(user2.getId()), List.of(request1, request2));
    }

    @Test
    void findById() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request1));
        assertNotNull(requestService.findById(request1.getId()));
        assertEquals(requestService.findById(request1.getId()), request1);
    }

    @Test
    void save() {
        when(requestRepository.save(any())).thenReturn(request1);
        assertNotNull(requestService.save(request1));
        assertEquals(requestService.save(request1), request1);
    }

    @Test
    void update() {
        when(requestRepository.save(any())).thenReturn(request1);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request1));
        assertNotNull(requestService.update(request1.getId(), request1));
        assertEquals(requestService.update(request1.getId(), request1), request1);
    }

    @Test
    void delete() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request1));
        assertNotNull(requestService.findById(request1.getId()));
        assertEquals(requestService.findById(request1.getId()), request1);
    }
}