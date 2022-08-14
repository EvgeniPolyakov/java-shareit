package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemIdGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private final ItemIdGenerator idGenerator;

    @Autowired
    public ItemStorageImpl(ItemIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<Item> getItemsByUserId(Long userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item get(Long id) {
        return items.get(id);
    }

    @Override
    public Item add(Item item) {
        generateId(item);
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item update(Long itemId, Item item) {
        item.setId(itemId);
        items.put(item.getId(), item);
        return items.get(itemId);
    }

    @Override
    public void delete(Long id) {
        items.remove(id);
    }

    private void generateId(Item item) {
        Long newId = idGenerator.generateItemId();
        item.setId(newId);
    }

    @Override
    public boolean checkItemOwner(Long itemId, Long userId) {
        return get(itemId).getOwner().equals(userId);
    }

    @Override
    public List<Item> search(String text) {
        return items.values()
                .stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(item -> item.getAvailable().equals(true))
                .collect(Collectors.toList());
    }
}
