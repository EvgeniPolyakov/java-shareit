package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> getAllByBookerIdOrderByStartDesc(Long id, Pageable pageable);

    List<Booking> getAllByBookerIdAndStatus(Long userId, Status status, Pageable pageable);

    List<Booking> getAllByBookerIdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime time, Pageable pageable);

    List<Booking> getAllByBookerIdAndStartAfterOrderByStartDesc(Long id, LocalDateTime time, Pageable pageable);

    List<Booking> getAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long id,
                                                                            LocalDateTime start,
                                                                            LocalDateTime end,
                                                                            Pageable pageable
    );

    List<Booking> getAllByItemOwnerIdOrderByStartDesc(Long id, Pageable pageable);

    List<Booking> getAllByItemOwnerIdAndStatus(Long userId, Status status, Pageable pageable);

    List<Booking> getAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime time, Pageable pageable);

    List<Booking> getAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime time, Pageable pageable);

    List<Booking> getAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId,
                                                                               LocalDateTime start,
                                                                               LocalDateTime end,
                                                                               Pageable pageable);

    Booking getFirstByItemIdOrderByStart(Long itemId);

    Booking getFirstByItemIdOrderByEndDesc(Long itemId);

    Booking getFirstByItemIdAndBookerIdOrderByStart(Long itemId, Long userId);

    boolean existsByBookerIdAndStatus(Long id, Status status);
}
