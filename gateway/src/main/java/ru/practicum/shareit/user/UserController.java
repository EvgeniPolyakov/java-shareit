package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.user.model.UserDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    public static final String ID_PATH_VARIABLE_KEY = "id";
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return userClient.getAll();
    }

    @PostMapping
    public ResponseEntity<Object> add(@Validated({Create.class}) @RequestBody UserDto userDto) {
        return userClient.add(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable(ID_PATH_VARIABLE_KEY) Long id, @Valid @RequestBody UserDto userDto) {
        return userClient.update(id, userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        return userClient.getById(id);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        userClient.remove(id);
    }
}
