package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
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

    public Booking add(BookingDto bookingDto) {
        Booking booking = BookingMapper.toBooking(bookingDto);
        log.info("Добавление нового бронирования с id {}", booking.getId());
        return bookingStorage.add(booking);
    }

    public Booking update(Long id, BookingDto bookingDto) {
        Booking bookingForUpdate = bookingStorage.getById(id);
        if (bookingDto.getItem() != null) {
            bookingForUpdate.setItem(bookingDto.getItem());
        }
        if (bookingDto.getStart() != null) {
            bookingForUpdate.setStart(bookingDto.getStart());
        }
        if (bookingDto.getEnd() != null) {
            bookingForUpdate.setEnd(bookingDto.getEnd());
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
