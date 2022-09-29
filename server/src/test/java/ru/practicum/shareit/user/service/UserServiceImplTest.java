package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    private UserService userService;
    private UserRepository userRepository;
    private final User user1 = new User(1L, "userName", "user@email.com");
    private final User user2 = new User(2L, "userName2", "user2@email.com");

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void getAll() {
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        assertNotNull(userService.getAll());
        assertEquals(2, userService.getAll().size());
        assertEquals(userService.getAll(), List.of(user1, user2));
    }

    @Test
    void save() {
        when(userRepository.save(any())).thenReturn(user1);
        assertNotNull(userService.save(user1));
        assertEquals(userService.save(user1), user1);
    }

    @Test
    void update() {
        when(userRepository.save(any())).thenReturn(user1);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        assertNotNull(userService.update(user1.getId(), user1));
        assertEquals(userService.update(user1.getId(), user1), user1);
    }

    @Test
    void updateWithWrongUserId() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.update(user1.getId(), user1));
    }

    @Test
    void delete() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        userService.delete(user1.getId());
        verify(userRepository, times(1)).delete(user1);
    }

    @Test
    void deleteWithWrongUserId() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.delete(user1.getId()));
    }

    @Test
    void findById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        assertNotNull(userService.findById(user1.getId()));
        assertEquals(userService.findById(user1.getId()), user1);
    }

    @Test
    void findByIdWithWrongId() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findById(user1.getId()));
    }
}