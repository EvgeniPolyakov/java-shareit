package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.ItemRequestDto;
import ru.practicum.shareit.requests.repository.ItemRequestStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestStorage requestStorage;

    public List<ItemRequest> getAll() {
        log.info("Получение списка всех заявок");
        return requestStorage.getAll();
    }

    public ItemRequest add(ItemRequestDto requestDto) {
        ItemRequest request = ItemRequestMapper.toItemRequest(requestDto);
        log.info("Добавление новой заявки с id {}", request.getId());
        return requestStorage.add(request);
    }

    public ItemRequest update(Long id, ItemRequestDto requestDto) {
        ItemRequest requestForUpdate = requestStorage.getById(id);
        if (requestDto.getDescription() != null) {
            requestForUpdate.setDescription(requestDto.getDescription());
        }
        if (requestDto.getRequester() != null) {
            requestForUpdate.setRequester(requestDto.getRequester());
        }
        log.info("Обновление заявки с id {}", id);
        return requestStorage.update(requestForUpdate);
    }

    public void delete(Long id) {
        log.info("Удаление заявки с id {}", id);
        requestStorage.delete(id);
    }

    public ItemRequest getById(Long id) {
        log.info("Получение бронирования с id {}", id);
        return requestStorage.getById(id);
    }
}
