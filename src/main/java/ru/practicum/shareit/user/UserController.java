package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Получен запрос GET /users");
        return UserMapper.toUserDtoList(userService.getAll());
    }

    @PostMapping
    public UserDto add(@Validated({Create.class}) @RequestBody UserDto user) {
        User addedUser = userService.add(user);
        log.info("Получен запрос POST (createUser). Добавлен пользователь: {}", user);
        return UserMapper.toUserDto(addedUser);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable("id") Long id, @Valid @RequestBody UserDto user) {
        User updatedUser = userService.update(id, user);
        log.info("Получен запрос PUT (updateUser). Добавлен пользователь: {}", updatedUser);
        return UserMapper.toUserDto(updatedUser);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable("id") Long id) {
        log.info("Получен запрос GET /users по id {}", id);
        return UserMapper.toUserDto(userService.getById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info("Получен запрос DELETE /users по id {}", id);
        userService.delete(id);
    }
}
