package ru.practicum.shareit.user.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserIdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final UserIdGenerator idGenerator;

    @Autowired
    public UserStorageImpl(UserIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getById(Long id) {
        return users.get(id);
    }

    @Override
    public User add(User user) {
        generateId(user);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User update(Long id, User user) {
        user.setId(id);
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    public boolean isEmailFree(User user) {
        return users.values()
                .stream()
                .map(User::getEmail)
                .anyMatch(s -> s.contains(user.getEmail()));
    }

    private void generateId(User user) {
        Long newId = idGenerator.generateUserId();
        user.setId(newId);
    }
}
