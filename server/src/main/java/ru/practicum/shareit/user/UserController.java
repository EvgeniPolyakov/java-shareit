package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    public static final String ID_PATH_VARIABLE_KEY = "id";

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Получен запрос GET по пути /users");
        return UserMapper.toUserDtoList(userService.getAll());
    }

    @PostMapping
    public UserDto add(@RequestBody UserDto userDto) {
        log.info("Получен запрос POST по пути /users для добавления пользователя: {}", userDto);
        User user = UserMapper.toUser(userDto);
        User addedUser = userService.save(user);
        return UserMapper.toUserDto(addedUser);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable(ID_PATH_VARIABLE_KEY) Long id, @RequestBody UserDto userDto) {
        log.info("Получен запрос PATCH по пути /users/{} для обновления пользователя: {}", id, userDto);
        User user = UserMapper.toUser(userDto);
        User updatedUser = userService.update(id, user);
        return UserMapper.toUserDto(updatedUser);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Получен запрос GET по пути /users по id {}", id);
        return UserMapper.toUserDto(userService.findById(id));
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Получен запрос DELETE по пути /users по id {}", id);
        userService.delete(id);
    }
}
