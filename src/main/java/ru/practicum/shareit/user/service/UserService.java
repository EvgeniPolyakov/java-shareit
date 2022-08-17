package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
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

    public User add(UserDto userDto) {
        isEmailFree(userDto);
        User user = UserMapper.toUser(userDto);
        log.info("Добавление нового пользователя с id {}", user.getId());
        return userStorage.add(user);
    }

    public User update(Long id, UserDto userDto) {
        checkUserId(id);
        User userForUpdate = userStorage.getById(id);
        if (userDto.getEmail() != null) {
            isEmailFree(userDto);
            userForUpdate.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            userForUpdate.setName(userDto.getName());
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

    private void isEmailFree(UserDto userDto) {
        if (userStorage.isEmailFree(userDto.getEmail())) {
            throw new ValidationException("Этот электронный адрес уже зарегистрирован.");
        }
    }


}
