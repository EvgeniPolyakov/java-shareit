package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.ValidationService;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.model.OutgoingItemDto;
import ru.practicum.shareit.requests.model.IncomingRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.OutgoingRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestMapper;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    public static final String ID_PATH_VARIABLE_KEY = "id";
    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final ItemRequestService itemRequestService;
    private final ValidationService validationService;
    private final ItemController itemController;
    private final UserService userService;

    @GetMapping
    public List<OutgoingRequestDto> getOwn(@RequestHeader(USER_HEADER) Long userId) {
        log.info("Получен запрос по пути GET /requests");
        validationService.validateUser(userId);
        List<ItemRequest> requests = itemRequestService.getOwn(userId);
        return requests.stream()
                .map(request -> appendRequestDto(request, userId))
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public List<OutgoingRequestDto> getAll(@RequestHeader(USER_HEADER) Long userId,
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
        log.info("Получен запрос по пути GET /requests/all");
        validationService.validateUser(userId);
        List<ItemRequest> requests = itemRequestService.getAll(from, size, userId);
        return requests.stream()
                .map(request -> appendRequestDto(request, userId))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OutgoingRequestDto getById(@RequestHeader(USER_HEADER) Long userId,
                                      @PathVariable(ID_PATH_VARIABLE_KEY) Long id) {
        log.info("Получен запрос GET по пути /requests по id {}", id);
        validationService.validateUser(userId);
        ItemRequest request = itemRequestService.findById(id);
        return appendRequestDto(request, userId);
    }

    @PostMapping
    public OutgoingRequestDto add(@RequestHeader(USER_HEADER) Long userId,
                                  @Valid @RequestBody IncomingRequestDto requestDto) {
        log.info("Получен запрос POST по пути /requests для добавления запроса на вещь: {}", requestDto);
        validationService.validateUser(userId);
        User user = userService.findById(userId);
        ItemRequest request = ItemRequestMapper.toItemRequest(requestDto, user);
        ItemRequest addedRequest = itemRequestService.save(request);
        return appendRequestDto(addedRequest, userId);
    }

    @PatchMapping("/{id}")
    public OutgoingRequestDto update(@RequestHeader(USER_HEADER) Long userId,
                                     @PathVariable(ID_PATH_VARIABLE_KEY) Long id,
                                     @Valid @RequestBody IncomingRequestDto requestDto) {
        log.info("Получен запрос PATCH по пути /requests для обновления запроса на вещь: {}", requestDto);
        validationService.validateUser(userId);
        User user = userService.findById(userId);
        ItemRequest request = ItemRequestMapper.toItemRequest(requestDto, user);
        ItemRequest updatedRequest = itemRequestService.update(id, request);
        return appendRequestDto(updatedRequest, userId);
    }

    private OutgoingRequestDto appendRequestDto(ItemRequest request, Long userId) {
        List<OutgoingItemDto> items = itemController.getAllByRequestId(request.getId(), userId);
        return ItemRequestMapper.toOutgoingRequestDto(request, items);
    }
}
