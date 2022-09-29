package ru.practicum.shareit.requests.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> getAllByRequesterIdOrderByCreatedDesc(Long id);

    List<ItemRequest> findAllByRequesterIdIsNot(Long userId, Pageable pageable);

}
