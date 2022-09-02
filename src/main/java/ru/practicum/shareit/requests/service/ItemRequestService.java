package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    List<ItemRequest> getAll();

    ItemRequest save(ItemRequest request);

    ItemRequest update(Long id, ItemRequest request);

    void delete(Long id);

    ItemRequest findById(Long id);
}
