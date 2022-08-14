package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.RequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.RequestStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final RequestStorage requestStorage;

    public List<ItemRequestDto> getAll() {
        log.info("Получение списка всех заявок");
        return RequestMapper.toItemRequestDtoList(requestStorage.getAll());
    }

    public ItemRequestDto add(ItemRequest request) {
        log.info("Добавление новой заявки с id {}", request.getId());
        return RequestMapper.toItemRequestDto(requestStorage.add(request));
    }

    public ItemRequestDto update(ItemRequest request) {
        log.info("Обновление заявки с id {}", request.getId());
        return RequestMapper.toItemRequestDto(requestStorage.update(request));
    }

    public void delete(Long id) {
        log.info("Удаление заявки с id {}", id);
        requestStorage.delete(id);
    }

    public ItemRequestDto getById(Long id) {
        log.info("Получение бронирования с id {}", id);
        return RequestMapper.toItemRequestDto(requestStorage.getById(id));
    }
}
