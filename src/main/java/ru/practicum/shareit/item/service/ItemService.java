package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
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

    public List<ItemDto> getItemsByUserId(Long userId) {
        userService.checkUserId(userId);
        log.info("Получение списка всех вещей пользователя {}", userId);
        List<Item> items = itemStorage.getItemsByUserId(userId);
        return ItemMapper.toItemDtoList(items);
    }

    public ItemDto add(Long userId, ItemDto itemDto) {
        userService.checkUserId(userId);
        validateItem(itemDto);
        Item item = ItemMapper.toItem(itemDto, userId);
        log.info("Добавление новой вещи с id {}", item.getId());
        return ItemMapper.toItemDto(itemStorage.add(item));
    }

    public ItemDto update(Long itemId, Long userId, ItemDto itemDto) {
        userService.checkUserId(userId);
        checkItemId(itemId);
        checkItemOwner(itemId, userId);
        Item itemForUpdate = itemStorage.get(itemId);
        if (itemDto.getDescription() != null) {
            validateStringField(itemDto.getDescription());
            itemForUpdate.setDescription(itemDto.getDescription());
        }
        if (itemDto.getName() != null) {
            validateStringField(itemDto.getName());
            itemForUpdate.setName(itemDto.getName());
        }
        if (itemDto.getAvailable() != null) {
            itemForUpdate.setAvailable(itemDto.getAvailable());
        }
        log.info("Обновление вещи с id {}", itemId);
        return ItemMapper.toItemDto(itemStorage.update(itemId, itemForUpdate));
    }

    public ItemDto get(Long userId, Long itemId) {
        checkItemId(itemId);
        userService.checkUserId(userId);
        log.info("Получение вещи с id {}", itemId);
        return ItemMapper.toItemDto(itemStorage.get(itemId));
    }

    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> items = itemStorage.search(text);
        return ItemMapper.toItemDtoList(items);
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
        if (!(itemStorage.checkItemOwner(itemId, userId))) {
            throw new ForbiddenException(String.format("У пользователя %s нет доступа к вещи %s.", itemId, userId));
        }
    }

    private void validateItem(ItemDto item) {
        checkRequiredFields(item);
        validateStringField(item.getName());
        validateStringField(item.getDescription());
    }

    private void checkRequiredFields(ItemDto item) {
        if (item.getAvailable() == null) {
            throw new BadRequestException("Не указано наличие вещи.");
        }
        if (item.getName() == null) {
            throw new BadRequestException("Не указано название вещи.");
        }
        if (item.getDescription() == null) {
            throw new BadRequestException("Не указано описание вещи.");
        }
    }

    private void validateStringField(String name) {
        if (name.isBlank()) {
            throw new BadRequestException("Не заполнено текстовое поле.");
        }
    }
}
