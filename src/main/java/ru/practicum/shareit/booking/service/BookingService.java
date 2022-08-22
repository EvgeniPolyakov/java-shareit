package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingStorage bookingStorage;

    public List<Booking> getAll() {
        log.info("Получение списка всех бронирований");
        return bookingStorage.getAll();
    }

    public Booking add(Booking booking) {
        log.info("Добавление нового бронирования с id {}", booking.getId());
        return bookingStorage.add(booking);
    }

    public Booking update(Long id, Booking booking) {
        Booking bookingForUpdate = bookingStorage.getById(id);
        if (booking.getItem() != null) {
            bookingForUpdate.setItem(booking.getItem());
        }
        if (booking.getStart() != null) {
            bookingForUpdate.setStart(booking.getStart());
        }
        if (booking.getEnd() != null) {
            bookingForUpdate.setEnd(booking.getEnd());
        }
        log.info("Обновление бронирования с id {}", id);
        return bookingStorage.update(bookingForUpdate);
    }

    public void delete(Long id) {
        log.info("Удаление бронирования с id {}", id);
        bookingStorage.delete(id);
    }

    public Booking getById(Long id) {
        log.info("Получение бронирования с id {}", id);
        return bookingStorage.getById(id);
    }
}
