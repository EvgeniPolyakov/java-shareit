package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingStorage bookingStorage;

    public List<BookingDto> getAll() {
        log.info("Получение списка всех бронирований");
        return BookingMapper.toBookingDtoList(bookingStorage.getAll());
    }

    public BookingDto add(Booking booking) {
        log.info("Добавление нового бронирования с id {}", booking.getId());
        return BookingMapper.toBookingDto(bookingStorage.add(booking));
    }

    public BookingDto update(Booking booking) {
        log.info("Обновление бронирования с id {}", booking.getId());
        return BookingMapper.toBookingDto(bookingStorage.update(booking));
    }

    public void delete(Long id) {
        log.info("Удаление бронирования с id {}", id);
        bookingStorage.delete(id);
    }

    public BookingDto getById(Long id) {
        log.info("Получение бронирования с id {}", id);
        return BookingMapper.toBookingDto(bookingStorage.getById(id));
    }
}
