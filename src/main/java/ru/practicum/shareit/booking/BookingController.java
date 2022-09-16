package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.ValidationService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String USER_HEADER = "X-Sharer-User-Id";
    private static final String STATUS_VALUE_ALL = "ALL";
    public static final String ID_PATH_VARIABLE_KEY = "id";

    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;
    private final ValidationService validationService;

    @GetMapping
    public List<OwnersBookingDto> getAllByBooker(@RequestHeader(USER_HEADER) Long userId,
                                                 @PositiveOrZero @RequestParam(
                                                         value = "from",
                                                         required = false,
                                                         defaultValue = "0"
                                                 ) Integer from,
                                                 @Min(1) @RequestParam(
                                                         value = "size",
                                                         required = false,
                                                         defaultValue = "5"
                                                 ) Integer size,
                                                 @RequestParam(defaultValue = STATUS_VALUE_ALL) String state) {
        log.info("Получен запрос GET по пути /bookings");
        validationService.validateUser(userId);
        QueryParam queryParam = validationService.validateQueryParam(state);
        List<Booking> bookings = bookingService.getBookingsMadeByUser(from, size, userId, queryParam);
        return BookingMapper.toOwnersBookingDtoList(bookings);
    }

    @GetMapping("/owner")
    public List<OwnersBookingDto> getAllForItemsOwned(@RequestHeader(USER_HEADER) Long userId,
                                                      @PositiveOrZero @RequestParam(
                                                              value = "from",
                                                              required = false,
                                                              defaultValue = "0"
                                                      ) Integer from,
                                                      @Min(1) @RequestParam(
                                                              value = "size",
                                                              required = false,
                                                              defaultValue = "5"
                                                      ) Integer size,
                                                      @RequestParam(defaultValue = STATUS_VALUE_ALL) String state) {
        log.info("Получен запрос GET по пути /bookings/owner");
        validationService.validateUser(userId);
        QueryParam queryParam = validationService.validateQueryParam(state);
        List<Booking> bookings = bookingService.getBookingsForItemsOwned(from, size, userId, queryParam);
        return BookingMapper.toOwnersBookingDtoList(bookings);
    }

    @GetMapping("/{id}")
    public OwnersBookingDto findById(@PathVariable(ID_PATH_VARIABLE_KEY) Long id, @RequestHeader(USER_HEADER) Long userId) {
        log.info("Получен запрос GET по пути /bookings по id {}", id);
        Booking booking = bookingService.findById(id, userId);
        validationService.validateBookingRequest(booking, userId);
        return BookingMapper.toOwnersBookingDto(booking);
    }

    @PostMapping
    public GuestBookingDto add(@Valid @RequestBody IncomingBookingDto bookingDto, @RequestHeader(USER_HEADER) Long userId) {
        log.info("Получен запрос POST по пути /bookings для добавления бронирования: {}", bookingDto);
        Item item = itemService.findById(bookingDto.getItemId());
        User user = userService.findById(userId);
        Booking bookingToPost = BookingMapper.toBooking(bookingDto, item, user);
        validationService.validateUser(userId);
        validationService.validateBooking(bookingToPost, userId, item);
        Booking bookingToReturn = bookingService.book(bookingToPost, userId);
        return BookingMapper.toGuestBookingDto(bookingToReturn);
    }

    @PatchMapping("/{id}")
    public OwnersBookingDto update(@PathVariable(ID_PATH_VARIABLE_KEY) Long bookingId,
                                   @RequestHeader(USER_HEADER) Long userId,
                                   @RequestParam boolean approved) {
        log.info("Получен запрос PATCH к бронированию с id {}", bookingId);
        Booking bookingForUpdate = bookingService.findById(bookingId, userId);
        validationService.validateStatusUpdateRequest(bookingForUpdate, userId);
        Booking bookingToReturn = bookingService.update(bookingForUpdate, userId, approved);
        return BookingMapper.toOwnersBookingDto(bookingToReturn);
    }
}
