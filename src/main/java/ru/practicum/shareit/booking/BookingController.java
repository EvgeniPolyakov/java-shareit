package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.IncomingBookingDto;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.ValidationService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;
    private final ValidationService validationService;

    private static final String USER_HEADER = "X-Sharer-User-Id";
    private static final String STATUS_VALUE_ALL = "ALL";

    @GetMapping
    public List<Booking> getAllByBooker(@RequestHeader(USER_HEADER) Long userId,
                                        @RequestParam(defaultValue = STATUS_VALUE_ALL) String state) {
        log.info("Получен запрос GET по пути /bookings");
        validationService.validateUser(userId);
        return bookingService.getBookingsMadeByUser(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getAllForItemsOwned(@RequestHeader(USER_HEADER) Long userId,
                                             @RequestParam(defaultValue = STATUS_VALUE_ALL) String state) {
        log.info("Получен запрос GET по пути /bookings/owner");
        validationService.validateUser(userId);
        return bookingService.getBookingsForItemsOwned(userId, state);
    }

    @GetMapping("/{id}")
    public Booking findById(@PathVariable("id") Long id, @RequestHeader(USER_HEADER) Long userId) {
        log.info("Получен запрос GET по пути /bookings по id {}", id);
        Booking booking = bookingService.findById(id, userId);
        validationService.validateBookingRequest(booking, id, userId);
        return booking;
    }

    @PostMapping
    public Booking add(@Valid @RequestBody IncomingBookingDto bookingDto, @RequestHeader(USER_HEADER) Long userId) {
        log.info("Получен запрос POST по пути /bookings для добавления бронирования: {}", bookingDto);
        Item item = itemService.findById(bookingDto.getItemId());
        User user = userService.findById(userId);
        Booking booking = BookingMapper.toBooking(bookingDto, item, user);
        validationService.validateUser(userId);
        validationService.validateBooking(booking, userId, item);
        return bookingService.save(booking, userId);
    }

    @PatchMapping("/{id}")
    public Booking update(@PathVariable("id") Long bookingId,
                          @RequestHeader(USER_HEADER) Long userId,
                          @RequestParam boolean approved) {
        log.info("Получен запрос PATCH к бронированию с id {}", bookingId);
        Booking booking = bookingService.findById(bookingId, userId);
        validationService.validateStatusUpdateRequest(booking, userId);
        return bookingService.update(booking, userId, approved);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id, @RequestHeader(USER_HEADER) Long userId) {
        log.info("Получен запрос DELETE по пути /bookings по id {}", id);
        Booking booking = bookingService.findById(id, userId);
        bookingService.delete(booking);
    }
}
