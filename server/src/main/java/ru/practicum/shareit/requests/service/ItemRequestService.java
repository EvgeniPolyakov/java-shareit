package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    List<ItemRequest> getAll(Integer from, Integer size, Long userId);

    ItemRequest save(ItemRequest request);

    ItemRequest update(Long id, ItemRequest request);

    ItemRequest findById(Long id);

    List<ItemRequest> getOwn(Long userId);
}
