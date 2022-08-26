package ru.practicum.shareit.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationService {
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    private static final String VALIDATION_STAGE_MESSAGE = "Этап валидации.";
    private static final String NO_ITEM_RIGHTS_MESSAGE = "У пользователя %s нет доступа к вещи %s.";
    private static final String NO_RIGHTS_TO_COMMENT_MESSAGE = "У пользователя нет прав комментировать.";
    private static final String OWNER_REQUESTING_OWN_ITEM_MESSAGE = "Запрос на бронирование отправлен хозяином вещи.";
    private static final String ITEM_NOT_AVAILABLE_MESSAGE = "На данный момент вещь не доступна для бронирования.";
    private static final String WRONG_DATE_MESSAGE = "Ошибка в дате бронирования.";
    private static final String NO_RIGHTS_FOR_BOOKING_MESSAGE = "У пользователя %s нет доступа к бронированию %s.";
    private static final String STATUS_ALREADY_ASSIGNED_MESSAGE = "Статус бронированию уже присвоен.";
    private static final String FIELD_IS_BLANK_MESSAGE = "Не заполнено текстовое поле.";

    public void validateItemOwner(Long itemId, Long userId) {
        log.info(VALIDATION_STAGE_MESSAGE);
        Item item = itemService.findById(itemId);
        if (!item.getOwner().getId().equals(userId)) {
            throw new ForbiddenException(String.format(NO_ITEM_RIGHTS_MESSAGE, itemId, userId));
        }
    }

    public boolean validateQueryString(String query) {
        log.info(VALIDATION_STAGE_MESSAGE);
        return query.isBlank();
    }

    public void validateComment(Comment comment, Long userId) {
        log.info(VALIDATION_STAGE_MESSAGE);
        if (!bookingService.isUserPresentAmongBookers(userId)) {
            throw new BadRequestException(NO_RIGHTS_TO_COMMENT_MESSAGE);
        }
        Booking booking = bookingService.findBookingByUserAndItem(comment.getItem().getId(), userId);
        if (comment.getCreationTime().isBefore(booking.getStart())) {
            throw new BadRequestException(NO_RIGHTS_TO_COMMENT_MESSAGE);
        }
    }

    public void validateStringField(String name) {
        log.info(VALIDATION_STAGE_MESSAGE);
        if (name.isBlank()) {
            throw new BadRequestException(FIELD_IS_BLANK_MESSAGE);
        }
    }

    public void validateBooking(Booking booking, Long userId, Item item) {
        log.info(VALIDATION_STAGE_MESSAGE);
        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException(OWNER_REQUESTING_OWN_ITEM_MESSAGE);
        }
        if (Boolean.FALSE.equals(item.getAvailable())) {
            throw new BadRequestException(ITEM_NOT_AVAILABLE_MESSAGE);
        }
        if (booking.getStart().isBefore(LocalDateTime.now()) ||
                booking.getEnd().isBefore(LocalDateTime.now()) ||
                booking.getEnd().isBefore(booking.getStart())
        ) {
            throw new BadRequestException(WRONG_DATE_MESSAGE);
        }
    }

    public void validateBookingRequest(Booking booking, Long id, Long userId) {
        log.info(VALIDATION_STAGE_MESSAGE);
        if (!userId.equals(booking.getBooker().getId()) && !userId.equals(booking.getItem().getOwner().getId())) {
            throw new NotFoundException(String.format(NO_RIGHTS_FOR_BOOKING_MESSAGE, userId, id));
        }
    }

    public void validateStatusUpdateRequest(Booking booking, Long userId) {
        log.info(VALIDATION_STAGE_MESSAGE);
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException(NO_ITEM_RIGHTS_MESSAGE);
        }
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new BadRequestException(STATUS_ALREADY_ASSIGNED_MESSAGE);
        }
    }

    public void validateUser(Long userId) {
        log.info(VALIDATION_STAGE_MESSAGE);
        userService.findById(userId);
    }
}
