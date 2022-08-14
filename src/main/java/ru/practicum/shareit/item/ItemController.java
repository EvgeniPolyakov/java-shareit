package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping()
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос GET /items");
        return itemService.getItemsByUserId(userId);
    }

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody Item item) {
        log.info("Получен запрос POST (createItem). Добавлена вещь: {}", item);
        return itemService.add(userId, item);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable("id") Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody Item item) {
        ItemDto updatedItem = itemService.update(itemId, userId, item);
        log.info("Получен запрос PUT (updateItem). Добавлена вещь: {}", updatedItem);
        return updatedItem;
    }

    @GetMapping("/{id}")
    public ItemDto get(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("id") Long itemId) {
        log.info("Получен запрос GET /items по id {}", itemId);
        return itemService.get(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text) {
        log.info("Получен запрос GET /items/search со значением {}", text);
        return itemService.search(text);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("id") Long itemId) {
        log.info("Получен запрос DELETE /items по id {}", itemId);
        itemService.delete(userId, itemId);
    }
}
