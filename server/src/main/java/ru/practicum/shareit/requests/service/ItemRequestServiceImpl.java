package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private static final String REQUEST_NOT_FOUND_MESSAGE = "Запрос c id %s не найден.";

    private final ItemRequestRepository repository;

    @Override
    public List<ItemRequest> getAll(Integer from, Integer size, Long userId) {
        log.info("Получение списка всех запросов");
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("created").descending());
        return repository.findAllByRequesterIdIsNot(userId, pageable);
    }

    @Override
    public List<ItemRequest> getOwn(Long userId) {
        log.info(String.format("Получение списка запросов пользователя %s", userId));
        return repository.getAllByRequesterIdOrderByCreatedDesc(userId);
    }

    @Override
    public ItemRequest findById(Long id) {
        log.info("Получение заявки с id {}", id);
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(REQUEST_NOT_FOUND_MESSAGE, id)));
    }

    @Override
    @Transactional
    public ItemRequest save(ItemRequest request) {
        log.info("Добавление новой заявки {}", request);
        return repository.save(request);
    }

    @Override
    @Transactional
    public ItemRequest update(Long id, ItemRequest request) {
        ItemRequest requestForUpdate = repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(REQUEST_NOT_FOUND_MESSAGE, id)));
        if (request.getDescription() != null) {
            requestForUpdate.setDescription(request.getDescription());
        }
        if (request.getRequester() != null) {
            requestForUpdate.setRequester(request.getRequester());
        }
        log.info("Обновление заявки с id {}", id);
        return repository.save(requestForUpdate);
    }
}
