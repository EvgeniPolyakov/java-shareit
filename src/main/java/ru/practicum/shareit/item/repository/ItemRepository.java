package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> getAllByOwnerIdOrderById(Long id, Pageable pageable);

    List<Item> getAllByRequestId(Long id);

    List<Item> getAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
            String nameQuery, String descriptionQuery, Pageable pageable);
}