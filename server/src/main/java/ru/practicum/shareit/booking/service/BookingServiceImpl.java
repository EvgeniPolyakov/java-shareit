package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QueryParam;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private static final String BOOKING_NOT_FOUND_MESSAGE = "Бронирование c id %s не найдено.";

    private final BookingRepository repository;

    @Override
    public List<Booking> getBookingsMadeByUser(Integer from, Integer size, Long userId, QueryParam queryParam) {
        log.info("Получение списка бронирований со статусом {} у пользователя {}", queryParam.name(), userId);
        Pageable pageable = PageRequest.of(from / size, size);
        switch (queryParam) {
            case WAITING:
                return repository.getAllByBookerIdAndStatus(userId, Status.WAITING, pageable);
            case REJECTED:
                return repository.getAllByBookerIdAndStatus(userId, Status.REJECTED, pageable);
            case PAST:
                return repository.getAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(), pageable);
            case FUTURE:
                return repository.getAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(), pageable);
            case CURRENT:
                return repository.getAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(),
                        LocalDateTime.now(), pageable);
            default:
                return repository.getAllByBookerIdOrderByStartDesc(userId, pageable);
        }
    }

    @Override
    public List<Booking> getBookingsForItemsOwned(Integer from, Integer size, Long userId, QueryParam queryParam) {
        log.info("Получение всех бронирований для вещей со статусом {} у пользователя {}", queryParam.name(), userId);
        Pageable pageable = PageRequest.of(from / size, size);
        switch (queryParam) {
            case WAITING:
                return repository.getAllByItemOwnerIdAndStatus(userId, Status.WAITING, pageable);
            case REJECTED:
                return repository.getAllByItemOwnerIdAndStatus(userId, Status.REJECTED, pageable);
            case PAST:
                return repository.getAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(), pageable);
            case FUTURE:
                return repository.getAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(), pageable);
            case CURRENT:
                return repository.getAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now(), pageable);
            default:
                return repository.getAllByItemOwnerIdOrderByStartDesc(userId, pageable);
        }
    }

    @Override
    public Booking getLastBooking(Long itemId, Long userId) {
        log.info("Получение последнего прошедшего бронирования у пользователя с id {}", userId);
        Booking booking = repository.getFirstByItemIdOrderByStart(itemId);
        return (booking != null && booking.getItem().getOwner().getId().equals(userId)) ? booking : null;
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
    public Booking book(Booking booking, Long userId) {
        log.info("Добавление нового бронирования: {}", booking);
        return repository.save(booking);
    }

    @Override
    @Transactional
    public Booking update(Booking booking, Long userId, boolean isBookingApproved) {
        log.info("Обновление бронирования с id {}", booking.getId());
        if (isBookingApproved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return repository.save(booking);
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