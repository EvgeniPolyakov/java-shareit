package ru.practicum.shareit.requests.repository;

import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemRequestStorage {

    List<ItemRequest> getAll();

    ItemRequest getById(Long id);

    ItemRequest add(ItemRequest request);

    ItemRequest update(ItemRequest request);

    void delete(Long id);
}
