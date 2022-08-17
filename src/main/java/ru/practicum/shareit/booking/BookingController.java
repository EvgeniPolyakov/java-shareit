package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.service.BookingMapper;
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
        return BookingMapper.toBookingDtoList(bookingService.getAll());
    }

    @PostMapping
    public BookingDto add(@Valid @RequestBody BookingDto booking) {
        log.info("Получен запрос POST (createBooking). Добавлено бронирование: {}", booking);
        return BookingMapper.toBookingDto(bookingService.add(booking));
    }

    @PatchMapping("/{id}")
    public BookingDto update(@PathVariable("id") Long id, @Valid @RequestBody BookingDto booking) {
        log.info("Получен запрос PUT (updateBooking). Добавлено бронирование: {}", booking);
        return BookingMapper.toBookingDto(bookingService.update(id, booking));
    }

    @GetMapping("/{id}")
    public BookingDto getById(@PathVariable("id") Long id) {
        log.info("Получен запрос GET /bookings по id {}", id);
        return BookingMapper.toBookingDto(bookingService.getById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info("Получен запрос DELETE /bookings по id {}", id);
        bookingService.delete(id);
    }
}
