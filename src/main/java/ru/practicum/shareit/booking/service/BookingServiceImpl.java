package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;

    private static final String STATUS_VALUE_ALL = "ALL";
    private static final String STATUS_VALUE_WAITING = "WAITING";
    private static final String STATUS_VALUE_REJECTED = "REJECTED";
    private static final String STATUS_VALUE_PAST = "PAST";
    private static final String STATUS_VALUE_FUTURE = "FUTURE";
    private static final String STATUS_VALUE_CURRENT = "CURRENT";
    private static final String UNKNOWN_STATUS_MESSAGE = "Unknown state: %s";
    private static final String BOOKING_NOT_FOUND_MESSAGE = "Бронирование c id %s не найдено.";

    @Override
    public List<Booking> getBookingsMadeByUser(Long userId, String status) {
        log.info("Получение списка бронирований со статусом {} у пользователя {}", status, userId);
        switch (status) {
            case STATUS_VALUE_ALL:
                return repository.getAllByBookerIdOrderByStartDesc(userId);
            case STATUS_VALUE_WAITING:
                return repository.getAllByBookerIdAndStatus(userId, Status.WAITING);
            case STATUS_VALUE_REJECTED:
                return repository.getAllByBookerIdAndStatus(userId, Status.REJECTED);
            case STATUS_VALUE_PAST:
                return repository.getAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
            case STATUS_VALUE_FUTURE:
                return repository.getAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
            case STATUS_VALUE_CURRENT:
                return repository.getAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(),
                        LocalDateTime.now());
            default:
                throw new BadRequestException(String.format(UNKNOWN_STATUS_MESSAGE, status));
        }
    }

    @Override
    public List<Booking> getBookingsForItemsOwned(Long userId, String status) {
        log.info("Получение всех бронирований для вещей со статусом {} у пользователя {}", status, userId);
        switch (status) {
            case STATUS_VALUE_ALL:
                return repository.getAllByItemOwnerIdOrderByStartDesc(userId);
            case STATUS_VALUE_WAITING:
                return repository.getAllByItemOwnerIdAndStatus(userId, Status.WAITING);
            case STATUS_VALUE_REJECTED:
                return repository.getAllByItemOwnerIdAndStatus(userId, Status.REJECTED);
            case STATUS_VALUE_PAST:
                return repository.getAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
            case STATUS_VALUE_FUTURE:
                return repository.getAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());
            case STATUS_VALUE_CURRENT:
                return repository.getAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now());
            default:
                throw new BadRequestException(String.format(UNKNOWN_STATUS_MESSAGE, status));
        }
    }

    @Override
    public Booking getLastBooking(Long itemId, Long userId) {
        log.info("Получение последнего прошедшего бронирования у пользователя с id {}", userId);
        Booking booking = repository.getFirstByItemIdOrderByStart(itemId);
        if (booking == null) {
            return null;
        }
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            return null;
        }
        return booking;
    }

    @Override
    public Booking getNextBooking(Long itemId, Long userId) {
        log.info("Получение первого будущего бронирования у пользователя с id {}", userId);
        Booking booking = repository.getFirstByItemIdOrderByEndDesc(itemId);
        if (booking == null) {
            return null;
        }
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            return null;
        }
        return booking;
    }

    @Override
    @Transactional
    public Booking save(Booking booking, Long userId) {
        log.info("Добавление нового бронирования с id {}", booking.getId());
        return repository.save(booking);
    }

    @Override
    @Transactional
    public Booking update(Booking booking, Long userId, boolean status) {
        log.info("Обновление бронирования с id {}", booking.getId());
        if (status) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return repository.save(booking);
    }

    @Override
    @Transactional
    public void delete(Booking booking) {
        log.info("Удаление бронирования с id {}", booking.getId());
        repository.delete(booking);
    }

    @Override
    public Booking findById(Long id, Long userId) {
        log.info("Получение бронирования с id {}", id);
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(BOOKING_NOT_FOUND_MESSAGE, id)));
    }

    @Override
    public boolean isUserPresentAmongBookers(Long id) {
        return repository.existsByBookerIdAndStatus(id, Status.APPROVED);
    }

    @Override
    public Booking findBookingByUserAndItem(Long itemId, Long userId) {
        return repository.getFirstByItemIdAndBookerIdOrderByStart(itemId, userId);
    }
}