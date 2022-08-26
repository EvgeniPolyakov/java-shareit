package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    private static final String USER_NOT_FOUND_MESSAGE = "Пользователь c id %s не найден.";

    @Override
    public List<User> getAll() {
        log.info("Получение списка всех пользователей");
        return repository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {
        log.info("Добавление пользователя с id {}", user.getId());
        return repository.save(user);
    }

    @Override
    @Transactional
    public User update(Long id, User user) {
        log.info("Обновление пользователя с id {}", id);
        User userForUpdate = repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, id)));
        if (user.getEmail() != null) {
            userForUpdate.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userForUpdate.setName(user.getName());
        }
        return repository.save(userForUpdate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Удаление пользователя с id {}", id);
        User user = repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, id)));
        repository.delete(user);
    }

    @Override
    public User findById(Long id) {
        log.info("Получение пользователя с id {}", id);
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, id)));
    }
}
