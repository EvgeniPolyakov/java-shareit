package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private static final String ITEM_NOT_FOUND_MESSAGE = "Вещь c id %s не найдена.";

    private final ItemRepository repository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Override
    public List<Item> getItemsByUserId(Integer from, Integer size, Long userId) {
        log.info("Получение списка всех вещей пользователя {}", userId);
        Pageable pageable = PageRequest.of(from / size, size);
        return repository.getAllByOwnerIdOrderById(userId, pageable);
    }

    @Override
    public List<Item> getAllByRequestId(Long id) {
        return repository.getAllByRequestId(id);
    }

    @Override
    @Transactional
    public Item save(Item item) {
        userService.findById(item.getOwner().getId());
        log.info("Добавление новой вещи: {}", item);
        return repository.save(item);
    }

    @Override
    @Transactional
    public Comment saveComment(Comment comment, Long userId) {
        log.info("Добавление нового комментария: {}", comment);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Item update(Long itemId, Long userId, Item item) {
        log.info("Обновление вещи с id {}", itemId);
        Item itemForUpdate = repository.findById(itemId).orElseThrow(
                () -> new NotFoundException(String.format(ITEM_NOT_FOUND_MESSAGE, itemId)));
        if (item.getDescription() != null) {
            itemForUpdate.setDescription(item.getDescription());
        }
        if (item.getName() != null) {
            itemForUpdate.setName(item.getName());
        }
        if (item.getAvailable() != null) {
            itemForUpdate.setAvailable(item.getAvailable());
        }
        return repository.save(itemForUpdate);
    }

    @Override
    public Item findById(Long itemId) {
        log.info("Получение вещи с id {}", itemId);
        return repository.findById(itemId).orElseThrow(
                () -> new NotFoundException(String.format(ITEM_NOT_FOUND_MESSAGE, itemId)));
    }

    @Override
    public List<Comment> findCommentsById(Long itemId) {
        log.info("Получение комментариев для вещи с id {}", itemId);
        return commentRepository.findByItemId(itemId);
    }

    @Override
    public List<Item> search(Integer from, Integer size, String query) {
        log.info("Поиск вещей по поисковому запросу: {}", query);
        Pageable pageable = PageRequest.of(from / size, size);
        return repository.getAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                query, query, pageable);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long itemId) {
        log.info("Удаление вещи с id {}", itemId);
        Item item = repository.findById(itemId).orElseThrow(
                () -> new NotFoundException(String.format(ITEM_NOT_FOUND_MESSAGE, itemId)));
        repository.delete(item);
    }
}
