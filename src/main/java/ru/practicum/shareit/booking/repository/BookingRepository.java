package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> getAllByBookerIdOrderByStartDesc(Long id);

    List<Booking> getAllByBookerIdAndStatus(Long userId, Status status);

    List<Booking> getAllByBookerIdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime time);

    List<Booking> getAllByBookerIdAndStartAfterOrderByStartDesc(Long id, LocalDateTime time);

    List<Booking> getAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long id,
                                                                            LocalDateTime start,
                                                                            LocalDateTime end);

    List<Booking> getAllByItemOwnerIdOrderByStartDesc(Long id);

    List<Booking> getAllByItemOwnerIdAndStatus(Long userId, Status status);

    List<Booking> getAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime time);

    List<Booking> getAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime time);

    List<Booking> getAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long userId,
                                                                               LocalDateTime start, LocalDateTime end);

    Booking getFirstByItemIdOrderByStart(Long itemId);

    Booking getFirstByItemIdOrderByEndDesc(Long itemId);

    Booking getFirstByItemIdAndBookerIdOrderByStart(Long itemId, Long userId);

    boolean existsByBookerIdAndStatus(Long id, Status status);
}
