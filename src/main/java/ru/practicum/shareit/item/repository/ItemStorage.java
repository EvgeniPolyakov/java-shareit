package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    List<Item> getItemsByUserId(Long userId);

    Item get(Long id);

    Item add(Item item);

    Item update(Long itemId, Item item);

    void delete(Long id);

    boolean isItemOwner(Long itemId, Long userId);

    List<Item> search(String text);
}