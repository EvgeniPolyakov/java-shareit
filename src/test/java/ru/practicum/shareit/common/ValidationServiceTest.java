package ru.practicum.shareit.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QueryParam;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidationServiceTest {
    private ValidationService validationService;
    private UserService userService;
    private ItemService itemService;
    private BookingService bookingService;

    private final User user1 = new User(1L, "user1", "user1@email.com");
    private final User user2 = new User(2L, "user2", "user2@email.com");
    private final ItemRequest request = new ItemRequest(1L, "description1", user1, LocalDateTime.now());
    private final Item item1 = new Item(1L, "item1", "text1", true, user1, request);
    private final Comment comment = new Comment(1L, item1, user1, "text", LocalDateTime.now());
    private final Item item2 = new Item(2L, "item2", "text2", false, user2, request);
    private final LocalDateTime start = LocalDateTime.now();
    private final LocalDateTime end = LocalDateTime.now();
    private final Booking booking = new Booking(1L, item1, start, end, user1, Status.WAITING);

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        itemService = mock(ItemService.class);
        bookingService = mock(BookingService.class);
        validationService = new ValidationService(userService, itemService, bookingService);
    }

    @Test
    void validateItemOwner() {
        when(itemService.findById(anyLong())).thenReturn(item1);
        assertThrows(ForbiddenException.class, () -> validationService.validateItemOwner(item1.getId(), user2.getId()));
    }

    @Test
    void validateQueryString() {
        assertTrue(validationService.validateQueryField(" "));
    }

    @Test
    void validateStringField() {
        assertThrows(BadRequestException.class, () -> validationService.validateStringField(""));
    }

    @Test
    void validateCommentWithWrongUser() {
        when(bookingService.isUserPresentAmongBookers(anyLong())).thenReturn(false);
        assertThrows(BadRequestException.class, () -> validationService.validateComment(comment, user2.getId()));
    }

    @Test
    void validateCommentWithWrongBookingCreationTime() {
        when(bookingService.isUserPresentAmongBookers(anyLong())).thenReturn(true);
        when(bookingService.findBookingByUserAndItem(anyLong(), anyLong())).thenReturn(booking);
        Comment backDateComment = new Comment(4L, item1, user1, "text", LocalDateTime.now().minusMinutes(1));
        assertThrows(BadRequestException.class, () -> validationService.validateComment(backDateComment, user2.getId()));
    }

    @Test
    void validateBooking() {
        assertThrows(NotFoundException.class, () -> validationService.validateBooking(booking, user1.getId(), item1));
        assertThrows(BadRequestException.class, () -> validationService.validateBooking(booking, user1.getId(), item2));
        item2.setAvailable(true);
        booking.setStart(LocalDateTime.now());
        assertThrows(BadRequestException.class, () -> validationService.validateBooking(booking, user1.getId(), item2));
        booking.setStart(LocalDateTime.now().plusMinutes(1));
        booking.setEnd(LocalDateTime.now());
        assertThrows(BadRequestException.class, () -> validationService.validateBooking(booking, user1.getId(), item2));
        booking.setEnd(LocalDateTime.now().minusMinutes(1));
        booking.setStart(LocalDateTime.now());
        assertThrows(BadRequestException.class, () -> validationService.validateBooking(booking, user1.getId(), item2));
    }

    @Test
    void validateBookingRequest() {
        assertThrows(NotFoundException.class, () -> validationService.validateBookingRequest(booking, user2.getId()));
        assertThrows(NotFoundException.class, () -> validationService.validateBookingRequest(booking, user2.getId()));
    }

    @Test
    void validateStatusUpdateRequest() {
        assertThrows(NotFoundException.class,
                () -> validationService.validateStatusUpdateRequest(booking, user2.getId()));
        booking.setStatus(Status.APPROVED);
        assertThrows(BadRequestException.class,
                () -> validationService.validateStatusUpdateRequest(booking, user1.getId()));
        booking.setStatus(Status.REJECTED);
        assertThrows(BadRequestException.class,
                () -> validationService.validateStatusUpdateRequest(booking, user1.getId()));
    }

    @Test
    void validateUser() {
        when(userService.findById(anyLong())).thenReturn(user1);
        validationService.validateUser(user1.getId());
    }

    @Test
    void validateQueryParam() {
        assertEquals(QueryParam.ALL, validationService.validateQueryParam("ALL"));
        assertEquals(QueryParam.WAITING, validationService.validateQueryParam("WAITING"));
        assertEquals(QueryParam.CURRENT, validationService.validateQueryParam("CURRENT"));
        assertEquals(QueryParam.PAST, validationService.validateQueryParam("PAST"));
        assertEquals(QueryParam.FUTURE, validationService.validateQueryParam("FUTURE"));
        assertEquals(QueryParam.REJECTED, validationService.validateQueryParam("REJECTED"));
        assertThrows(BadRequestException.class, () -> validationService.validateQueryParam("OTHER STATUS"));
    }
}