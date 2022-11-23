package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.item.model.IncomingCommentDto;
import ru.practicum.shareit.item.model.IncomingItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private static final String USER_HEADER = "X-Sharer-User-Id";
    public static final String ID_PATH_VARIABLE_KEY = "id";
    public static final String TEXT_PARAM = "text";
    public static final String FROM_PARAM = "from";
    public static final String SIZE_PARAM = "size";

    private final ItemClient itemClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@PathVariable(ID_PATH_VARIABLE_KEY) Long itemId,
                                          @RequestHeader(USER_HEADER) Long userId) {
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(USER_HEADER) Long userId,
                                         @PositiveOrZero @RequestParam(
                                                 value = FROM_PARAM,
                                                 required = false,
                                                 defaultValue = "0")
                                         Integer from,
                                         @Min(1) @RequestParam(
                                                 value = SIZE_PARAM,
                                                 required = false,
                                                 defaultValue = "10")
                                         Integer size) {
        return itemClient.getAll(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(USER_HEADER) Long userId, @RequestParam(TEXT_PARAM) String text,
                                         @RequestParam(
                                                 value = FROM_PARAM,
                                                 required = false,
                                                 defaultValue = "0")
                                         Integer from,
                                         @RequestParam(
                                                 value = SIZE_PARAM,
                                                 required = false,
                                                 defaultValue = "10")
                                         Integer size) {
        return itemClient.search(userId, text, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@Validated({Create.class})
                                          @Valid @RequestBody IncomingItemDto dto,
                                          @RequestHeader(USER_HEADER) Long userId) {
        return itemClient.addItem(userId, dto);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(@PathVariable(ID_PATH_VARIABLE_KEY) Long itemId,
                                             @Valid @RequestBody IncomingCommentDto dto,
                                             @RequestHeader(USER_HEADER) Long userId) {
        return itemClient.addComment(userId, itemId, dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable(ID_PATH_VARIABLE_KEY) Long itemId,
                                         @RequestHeader(USER_HEADER) Long userId,
                                         @RequestBody IncomingItemDto dto) {
        return itemClient.update(userId, itemId, dto);
    }
}