package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private static final String REQUEST_NOT_FOUND_MESSAGE = "Заявка c id %s не найдена.";

    private final ItemRequestRepository repository;

    @Override
    public List<ItemRequest> getAll() {
        log.info("Получение списка всех заявок");
        return repository.findAll();
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
        if (request.getRequesterId() != null) {
            requestForUpdate.setRequesterId(request.getRequesterId());
        }
        log.info("Обновление заявки с id {}", id);
        return repository.save(requestForUpdate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ItemRequest request = repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(REQUEST_NOT_FOUND_MESSAGE, id)));
        log.info("Удаление заявки с id {}", id);
        repository.delete(request);
    }

    @Override
    public ItemRequest findById(Long id) {
        log.info("Получение заявки с id {}", id);
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(REQUEST_NOT_FOUND_MESSAGE, id)));
    }
}
