package ru.practicum.shareit.item.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> getItemsByUserId(Integer from, Integer size, Long userId);

    Item save(Item item);

    @Transactional
    Comment saveComment(Comment comment, Long userId);

    Item update(Long itemId, Long userId, Item item);

    Item findById(Long itemId);

    List<Comment> findCommentsById(Long itemId);

    List<Item> search(Integer from, Integer size, String text);

    List<Item> getAllByRequestId(Long id);

    void delete(Long userId, Long itemId);
}
