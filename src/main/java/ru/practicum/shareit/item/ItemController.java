package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.ValidationService;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.service.CommentMapper;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final ValidationService validationService;

    private static final String USER_HEADER = "X-Sharer-User-Id";

    @GetMapping()
    public List<OutgoingItemDto> getAll(@RequestHeader(USER_HEADER) Long userId) {
        log.info("Получен запрос GET по пути /items");
        List<Item> items = itemService.getItemsByUserId(userId);
        return itemMapper.toOutgoingItemDtoList(items, userId);
    }

    @GetMapping("/{id}")
    public OutgoingItemDto get(@PathVariable("id") Long itemId, @RequestHeader(USER_HEADER) Long userId) {
        log.info("Получен запрос GET по пути /items по id {}", itemId);
        Item item = itemService.findById(itemId);
        return itemMapper.toOutgoingItemDto(item, userId);
    }

    @PostMapping
    public OutgoingItemDto addItem(@Validated({Create.class})
                                   @RequestBody IncomingItemDto incomingItemDto,
                                   @RequestHeader(USER_HEADER) Long userId) {
        log.info("Получен запрос POST по пути /items для добавления вещи: {}", incomingItemDto);
        Item item = itemMapper.toItem(incomingItemDto, userId);
        Item addedItem = itemService.save(item);
        return itemMapper.toOutgoingItemDto(addedItem, userId);
    }

    @PostMapping("/{id}/comment")
    public OutgoingCommentDto addComment(@PathVariable("id") Long itemId,
                                         @Valid @RequestBody IncomingCommentDto incomingCommentDto,
                                         @RequestHeader(USER_HEADER) Long userId) {
        Comment comment = commentMapper.toComment(incomingCommentDto, itemId, userId);
        log.info("Получен запрос POST по пути /items/{}/comment для добавления комментария: {}", itemId, comment);
        validationService.validateComment(comment, userId);
        validationService.validateUser(comment.getAuthor().getId());
        validationService.validateStringField(comment.getText());
        Comment addedComment = itemService.saveComment(comment, userId);
        return CommentMapper.toOutgoingCommentDto(addedComment);
    }

    @PatchMapping("/{id}")
    public OutgoingItemDto update(@PathVariable("id") Long itemId,
                                  @RequestHeader(USER_HEADER) Long userId,
                                  @RequestBody IncomingItemDto incomingItemDto) {
        log.info("Получен запрос PATCH по пути /items/{} для обновления вещи: {}", itemId, incomingItemDto);
        Item item = itemMapper.toItem(incomingItemDto, userId);
        validationService.validateItemOwner(itemId, userId);
        validationService.validateUser(item.getOwner().getId());
        Item updatedItem = itemService.update(itemId, userId, item);
        return itemMapper.toOutgoingItemDto(updatedItem, userId);
    }

    @GetMapping("/search")
    public List<OutgoingItemDto> search(@RequestHeader(USER_HEADER) Long userId, @RequestParam("text") String text) {
        log.info("Получен запрос GET по пути /items/search со значением {}", text);
        if (validationService.validateQueryString(text)) {
            return new ArrayList<>();
        }
        List<Item> items = itemService.search(text);
        return itemMapper.toOutgoingItemDtoList(items, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader(USER_HEADER) Long userId,
                       @PathVariable("id") Long itemId) {
        log.info("Получен запрос DELETE по пути /items по id {}", itemId);
        validationService.validateItemOwner(itemId, userId);
        itemService.delete(userId, itemId);
    }
}
