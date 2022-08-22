package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    public List<Item> getItemsByUserId(Long userId) {
        userService.checkUserId(userId);
        log.info("Получение списка всех вещей пользователя {}", userId);
        return itemStorage.getItemsByUserId(userId);
    }

    public Item add(Long userId, Item item) {
        userService.checkUserId(userId);
        log.info("Добавление новой вещи с id {}", item.getId());
        return itemStorage.add(item);
    }

    public Item update(Long itemId, Long userId, Item item) {
        userService.checkUserId(userId);
        checkItemId(itemId);
        checkItemOwner(itemId, userId);
        Item itemForUpdate = itemStorage.get(itemId);
        if (item.getDescription() != null) {
            validateStringField(item.getDescription());
            itemForUpdate.setDescription(item.getDescription());
        }
        if (item.getName() != null) {
            validateStringField(item.getName());
            itemForUpdate.setName(item.getName());
        }
        if (item.getAvailable() != null) {
            itemForUpdate.setAvailable(item.getAvailable());
        }
        log.info("Обновление вещи с id {}", itemId);
        return itemStorage.update(itemId, itemForUpdate);
    }

    public Item get(Long userId, Long itemId) {
        checkItemId(itemId);
        userService.checkUserId(userId);
        log.info("Получение вещи с id {}", itemId);
        return itemStorage.get(itemId);
    }

    public List<Item> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemStorage.search(text);
    }

    private void checkItemId(Long itemId) {
        if (itemStorage.get(itemId) == null) {
            throw new NotFoundException(String.format("Вещь c id %s не найдена.", itemId));
        }
    }

    public void delete(Long userId, Long itemId) {
        userService.checkUserId(userId);
        checkItemId(itemId);
        checkItemOwner(itemId, userId);
        log.info("Удаление вещи с id {}", itemId);
        itemStorage.delete(itemId);
    }

    private void checkItemOwner(Long itemId, Long userId) {
        if (!(itemStorage.isItemOwner(itemId, userId))) {
            throw new ForbiddenException(String.format("У пользователя %s нет доступа к вещи %s.", itemId, userId));
        }
    }

    private void validateStringField(String name) {
        if (name.isBlank()) {
            throw new BadRequestException("Не заполнено текстовое поле.");
        }
    }
}
