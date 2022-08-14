package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();

    User getById(Long id);

    User add(User user);

    User update(Long id, User user);

    void delete(Long id);

    boolean isEmailFree(User user);
}
