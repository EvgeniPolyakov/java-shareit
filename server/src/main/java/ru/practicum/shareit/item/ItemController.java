package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.GuestBookingDto;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.ValidationService;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.service.CommentMapper;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String USER_HEADER = "X-Sharer-User-Id";
    public static final String ID_PATH_VARIABLE_KEY = "id";
    public static final String TEXT_PARAM = "text";
    public static final String FROM_PARAM = "from";
    public static final String SIZE_PARAM = "size";

    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final ValidationService validationService;
    private final ItemRequestService requestService;

    @GetMapping()
    public List<OutgoingItemDto> getAll(@RequestHeader(USER_HEADER) Long userId,
                                        @RequestParam(
                                                value = FROM_PARAM,
                                                required = false,
                                                defaultValue = "0")
                                        Integer from,
                                        @RequestParam(
                                                value = SIZE_PARAM,
                                                required = false,
                                                defaultValue = "10")
                                        Integer size) {
        log.info("Получен запрос GET по пути /items");
        List<Item> items = itemService.getItemsByUserId(from, size, userId);
        return items.stream()
                .map(item -> appendItemDto(item.getId(), userId))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OutgoingItemDto getItem(@PathVariable(ID_PATH_VARIABLE_KEY) Long itemId, @RequestHeader(USER_HEADER) Long userId) {
        log.info("Получен запрос GET по пути /items по id {}", itemId);
        return appendItemDto(itemId, userId);
    }

    @PostMapping
    public OutgoingItemDto addItem(@RequestBody IncomingItemDto incomingItemDto,
                                   @RequestHeader(USER_HEADER) Long userId) {
        log.info("Получен запрос POST по пути /items для добавления вещи: {}", incomingItemDto);
        User user = userService.findById(userId);
        Item item = ItemMapper.toItem(incomingItemDto, user);
        if (incomingItemDto.getRequestId() != null) {
            item.setRequest(requestService.findById(incomingItemDto.getRequestId()));
        }
        Item addedItem = itemService.save(item);
        return appendItemDto(addedItem.getId(), userId);
    }

    @PostMapping("/{id}/comment")
    public OutgoingCommentDto addComment(@PathVariable(ID_PATH_VARIABLE_KEY) Long itemId,
                                         @RequestBody IncomingCommentDto commentDto,
                                         @RequestHeader(USER_HEADER) Long userId) {
        log.info("Получен запрос POST по пути /items/{}/comment для добавления комментария: {}", itemId, commentDto);
        Item item = itemService.findById(itemId);
        User user = userService.findById(userId);
        Comment commentToAdd = CommentMapper.toComment(commentDto, item, user);
        validationService.validateComment(commentToAdd, userId);
        validationService.validateUser(commentToAdd.getAuthor().getId());
        validationService.validateStringField(commentToAdd.getText());
        Comment addedComment = itemService.saveComment(commentToAdd, userId);
        return CommentMapper.toOutgoingCommentDto(addedComment);
    }

    @PatchMapping("/{id}")
    public OutgoingItemDto update(@PathVariable(ID_PATH_VARIABLE_KEY) Long itemId,
                                  @RequestHeader(USER_HEADER) Long userId,
                                  @RequestBody IncomingItemDto incomingItemDto) {
        log.info("Получен запрос PATCH по пути /items/{} для обновления вещи: {}", itemId, incomingItemDto);
        User user = userService.findById(userId);
        Item item = ItemMapper.toItem(incomingItemDto, user);
        validationService.validateItemOwner(itemId, userId);
        validationService.validateUser(item.getOwner().getId());
        if (incomingItemDto.getRequestId() != null) {
            item.setRequest(requestService.findById(incomingItemDto.getRequestId()));
        }
        Item updatedItem = itemService.update(itemId, userId, item);
        return appendItemDto(updatedItem.getId(), userId);
    }

    @GetMapping("/search")
    public List<OutgoingItemDto> search(@RequestHeader(USER_HEADER) Long userId, @RequestParam(TEXT_PARAM) String text,
                                        @RequestParam(
                                                value = FROM_PARAM,
                                                required = false,
                                                defaultValue = "0")
                                        Integer from,
                                        @RequestParam(
                                                value = SIZE_PARAM,
                                                required = false,
                                                defaultValue = "10")
                                        Integer size) {
        log.info("Получен запрос GET по пути /items/search со значением {}", text);
        if (validationService.validateQueryField(text)) {
            return new ArrayList<>();
        }
        List<Item> items = itemService.search(from, size, text);
        return items.stream()
                .map(item -> appendItemDto(item.getId(), userId))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void remove(@RequestHeader(USER_HEADER) Long userId,
                       @PathVariable(ID_PATH_VARIABLE_KEY) Long itemId) {
        log.info("Получен запрос DELETE по пути /items по id {}", itemId);
        validationService.validateItemOwner(itemId, userId);
        itemService.delete(userId, itemId);
    }

    public List<OutgoingItemDto> getAllByRequestId(Long requestId, Long userId) {
        List<Item> items = itemService.getAllByRequestId(requestId);
        return items.stream()
                .map(item -> appendItemDto(item.getId(), userId))
                .collect(Collectors.toList());
    }

    private OutgoingItemDto appendItemDto(Long itemId, Long userId) {
        Item item = itemService.findById(itemId);
        Booking lastBooking = bookingService.getLastBooking(itemId, userId);
        GuestBookingDto lastBookingDto = BookingMapper.toGuestBookingDto(lastBooking);
        Booking nextBooking = bookingService.getNextBooking(itemId, userId);
        GuestBookingDto nextBookingDto = BookingMapper.toGuestBookingDto(nextBooking);
        List<Comment> comments = itemService.findCommentsById(itemId);
        List<OutgoingCommentDto> commentsDto = CommentMapper.toOutgoingCommentDtoList(comments);
        return ItemMapper.toOutgoingItemDto(item, lastBookingDto, nextBookingDto, commentsDto);
    }
}
