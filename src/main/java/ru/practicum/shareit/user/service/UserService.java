package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public List<User> getAll() {
        log.info("Получение списка всех пользователей");
        return userStorage.getAll();
    }

    public User add(User user) {
        isEmailFree(user);
        log.info("Добавление нового пользователя с id {}", user.getId());
        return userStorage.add(user);
    }

    public User update(Long id, User user) {
        checkUserId(id);
        User userForUpdate = userStorage.getById(id);
        if (user.getEmail() != null) {
            isEmailFree(user);
            userForUpdate.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userForUpdate.setName(user.getName());
        }
        log.info("Обновление пользователя с id {}", id);
        return userStorage.update(id, userForUpdate);
    }

    public void delete(Long id) {
        checkUserId(id);
        log.info("Удаление пользователя с id {}", id);
        userStorage.delete(id);
    }

    public User getById(Long id) {
        checkUserId(id);
        log.info("Получение пользователя с id {}", id);
        return userStorage.getById(id);
    }

    public void checkUserId(Long id) {
        if (userStorage.getById(id) == null) {
            throw new NotFoundException(String.format("Пользователь c id %s не найден.", id));
        }
    }

    private void isEmailFree(User user) {
        if (userStorage.isEmailFree(user.getEmail())) {
            throw new ValidationException("Этот электронный адрес уже зарегистрирован.");
        }
    }


}
