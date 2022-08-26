package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ItemRepository repository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private static final String ITEM_NOT_FOUND_MESSAGE = "Вещь c id %s не найдена.";

    @Override
    public List<Item> getItemsByUserId(Long userId) {
        log.info("Получение списка всех вещей пользователя {}", userId);
        return repository.getAllByOwnerIdOrderById(userId);
    }

    @Override
    @Transactional
    public Item save(Item item) {
        userService.findById(item.getOwner().getId());
        log.info("Добавление новой вещи с id {}", item.getId());
        return repository.save(item);
    }

    @Override
    @Transactional
    public Comment saveComment(Comment comment, Long userId) {
        log.info("Добавление нового комментария с id {}", comment.getId());
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
    public List<Item> search(String query) {
        log.info("Поиск вещей по поисковому запросу: {}", query);
        return repository.getAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                query, query);
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
