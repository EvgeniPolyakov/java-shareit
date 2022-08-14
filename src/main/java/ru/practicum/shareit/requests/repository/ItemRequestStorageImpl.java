package ru.practicum.shareit.requests.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestIdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRequestStorageImpl implements ItemRequestStorage {
    private final Map<Long, ItemRequest> requests = new HashMap<>();
    private final ItemRequestIdGenerator idGenerator;

    @Autowired
    public ItemRequestStorageImpl(ItemRequestIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<ItemRequest> getAll() {
        return new ArrayList<>(requests.values());
    }

    @Override
    public ItemRequest getById(Long id) {
        return requests.get(id);
    }

    @Override
    public ItemRequest add(ItemRequest request) {
        generateId(request);
        requests.put(request.getId(), request);
        return requests.get(request.getId());
    }

    @Override
    public ItemRequest update(ItemRequest request) {
        requests.put(request.getId(), request);
        return requests.get(request.getId());
    }

    @Override
    public void delete(Long id) {
        requests.remove(id);
    }

    private void generateId(ItemRequest request) {
        Long newId = idGenerator.generateRequestId();
        request.setId(newId);
    }
}
