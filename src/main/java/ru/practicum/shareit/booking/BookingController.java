package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public List<BookingDto> getAll() {
        log.info("Получен запрос GET /bookings");
        return bookingService.getAll();
    }

    @PostMapping
    public BookingDto add(@Valid @RequestBody Booking booking) {
        log.info("Получен запрос POST (createBooking). Добавлено бронирование: {}", booking);
        return bookingService.add(booking);
    }

    @PatchMapping("/{id}")
    public BookingDto update(@Valid @RequestBody Booking booking) {
        log.info("Получен запрос PUT (updateBooking). Добавлено бронирование: {}", booking);
        return bookingService.update(booking);
    }

    @GetMapping("/{id}")
    public BookingDto getById(@PathVariable("id") Long id) {
        log.info("Получен запрос GET /bookings по id {}", id);
        return bookingService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info("Получен запрос DELETE /bookings по id {}", id);
        bookingService.delete(id);
    }
}
