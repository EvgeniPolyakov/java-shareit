package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    private static final String USER_HEADER = "X-Sharer-User-Id";

    @GetMapping()
    public List<ItemDto> getAll(@RequestHeader(USER_HEADER) Long userId) {
        log.info("Получен запрос GET /items");
        List<Item> items = itemService.getItemsByUserId(userId);
        return ItemMapper.toItemDtoList(items);
    }

    @PostMapping
    public ItemDto add(@Validated({Create.class}) @RequestBody ItemDto item, @RequestHeader(USER_HEADER) Long userId) {
        log.info("Получен запрос POST (createItem). Добавлена вещь: {}", item);
        Item addedItem = itemService.add(userId, item);
        return ItemMapper.toItemDto(addedItem);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable("id") Long itemId,
                          @RequestHeader(USER_HEADER) Long userId,
                          @RequestBody ItemDto item) {
        Item updatedItem = itemService.update(itemId, userId, item);
        log.info("Получен запрос PUT (updateItem). Добавлена вещь: {}", updatedItem);
        return ItemMapper.toItemDto(updatedItem);
    }

    @GetMapping("/{id}")
    public ItemDto get(@RequestHeader(USER_HEADER) Long userId, @PathVariable("id") Long itemId) {
        log.info("Получен запрос GET /items по id {}", itemId);
        Item item = itemService.get(userId, itemId);
        return ItemMapper.toItemDto(item);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text) {
        log.info("Получен запрос GET /items/search со значением {}", text);
        List<Item> items = itemService.search(text);
        return ItemMapper.toItemDtoList(items);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader(USER_HEADER) Long userId, @PathVariable("id") Long itemId) {
        log.info("Получен запрос DELETE /items по id {}", itemId);
        itemService.delete(userId, itemId);
    }
}
