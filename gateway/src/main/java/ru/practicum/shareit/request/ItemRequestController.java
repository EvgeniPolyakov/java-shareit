package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.IncomingRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    public static final String ID_PATH_VARIABLE_KEY = "id";
    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final RequestClient requestClient;

    @GetMapping
    public ResponseEntity<Object> getOwn(@RequestHeader(USER_HEADER) Long userId) {
        return requestClient.getOwn(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader(USER_HEADER) Long userId,
                                         @PositiveOrZero @RequestParam(
                                                 value = "from",
                                                 required = false,
                                                 defaultValue = "0"
                                         ) Integer from,
                                         @Min(1) @RequestParam(
                                                 value = "size",
                                                 required = false,
                                                 defaultValue = "5"
                                         ) Integer size) {
        return requestClient.getAll(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@RequestHeader(USER_HEADER) Long userId,
                                          @PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        return requestClient.getById(userId, id);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(USER_HEADER) Long userId,
                                      @Valid @RequestBody IncomingRequestDto requestDto) {
        return requestClient.add(userId, requestDto);
    }

}
