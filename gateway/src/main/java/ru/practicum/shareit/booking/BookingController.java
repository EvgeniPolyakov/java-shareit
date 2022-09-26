package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.IncomingBookingDto;
import ru.practicum.shareit.booking.model.QueryParam;
import ru.practicum.shareit.exception.BadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private static final String USER_HEADER = "X-Sharer-User-Id";
    private static final String STATUS_VALUE_ALL = "ALL";
    public static final String ID_PATH_VARIABLE_KEY = "id";
    public static final String FROM_PARAM = "from";
    public static final String SIZE_PARAM = "size";
    private static final String UNKNOWN_STATUS_MESSAGE = "Unknown state: %s";

    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getAllByBooker(@RequestHeader(USER_HEADER) Long userId,
                                                 @PositiveOrZero @RequestParam(
                                                         value = FROM_PARAM,
                                                         required = false,
                                                         defaultValue = "0"
                                                 ) Integer from,
                                                 @Min(1) @RequestParam(
                                                         value = SIZE_PARAM,
                                                         required = false,
                                                         defaultValue = "10"
                                                 ) Integer size,
                                                 @RequestParam(defaultValue = STATUS_VALUE_ALL) String state) {
        QueryParam query = QueryParam.from(state)
                .orElseThrow(() -> new BadRequestException(String.format(UNKNOWN_STATUS_MESSAGE, state)));
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getAllByBooker(userId, query, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllForItemsOwned(@RequestHeader(USER_HEADER) Long userId,
                                                      @PositiveOrZero @RequestParam(
                                                              value = FROM_PARAM,
                                                              required = false,
                                                              defaultValue = "0"
                                                      ) Integer from,
                                                      @Min(1) @RequestParam(
                                                              value = SIZE_PARAM,
                                                              required = false,
                                                              defaultValue = "10"
                                                      ) Integer size,
                                                      @RequestParam(defaultValue = STATUS_VALUE_ALL) String state) {
        QueryParam query = QueryParam.from(state)
                .orElseThrow(() -> new BadRequestException(String.format(UNKNOWN_STATUS_MESSAGE, state)));
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getAllForItemsOwned(userId, query, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody IncomingBookingDto bookingDto,
                                      @RequestHeader(USER_HEADER) Long userId) {
        log.info("Creating booking {}, userId={}", bookingDto, userId);
        return bookingClient.add(userId, bookingDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(ID_PATH_VARIABLE_KEY) Long id, @RequestHeader(USER_HEADER) Long userId) {
        log.info("Get booking {}, userId={}", id, userId);
        return bookingClient.findById(userId, id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable(ID_PATH_VARIABLE_KEY) Long bookingId,
                                         @RequestHeader(USER_HEADER) Long userId,
                                         @RequestParam boolean approved) {
        log.info("Patch booking with userId={}, bookingId={}, approved={}", userId, bookingId, approved);
        return bookingClient.update(userId, bookingId, approved);
    }
}
