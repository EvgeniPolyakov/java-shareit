package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.booking.model.QueryParam.*;

class BookingServiceImplTest {
    private BookingService bookingService;
    private BookingRepository bookingRepository;

    private final User user = new User(1L, "user1", "user1@email.com");
    private final Item item = new Item(1L, "item1", "text1", true, user, null);
    private final Booking booking1 = new Booking(
            1L,
            item,
            LocalDateTime.now().plusMinutes(1),
            LocalDateTime.now().plusMinutes(2),
            user,
            Status.WAITING
    );
    private final Booking booking2 = new Booking(
            3L,
            item,
            LocalDateTime.now().minusMinutes(2),
            LocalDateTime.now().minusMinutes(1),
            user,
            Status.APPROVED
    );

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository);
    }

    @Test
    void getBookingsMadeByUser() {
        when(bookingRepository.getAllByBookerIdAndStatus(anyLong(), any(), any()))
                .thenReturn(List.of(booking1));
        assertNotNull(bookingService.getBookingsMadeByUser(0, 5, user.getId(), WAITING));
        assertEquals(1, bookingService.getBookingsMadeByUser(0, 5, user.getId(), WAITING).size());
        assertEquals(bookingService.getBookingsMadeByUser(0, 5, user.getId(), WAITING), List.of(booking1));

        when(bookingRepository.getAllByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking1));
        assertNotNull(bookingService.getBookingsMadeByUser(0, 5, user.getId(), REJECTED));
        assertEquals(1, bookingService.getBookingsMadeByUser(0, 5, user.getId(), REJECTED).size());
        assertEquals(bookingService.getBookingsMadeByUser(0, 5, user.getId(), REJECTED), List.of(booking1));

        when(bookingRepository.getAllByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking1));
        assertNotNull(bookingService.getBookingsMadeByUser(0, 5, user.getId(), FUTURE));
        assertEquals(1, bookingService.getBookingsMadeByUser(0, 5, user.getId(), FUTURE).size());
        assertEquals(bookingService.getBookingsMadeByUser(0, 5, user.getId(), FUTURE), List.of(booking1));

        when(bookingRepository.getAllByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking1));
        assertNotNull(bookingService.getBookingsMadeByUser(0, 5, user.getId(), PAST));
        assertEquals(1, bookingService.getBookingsMadeByUser(0, 5, user.getId(), PAST).size());
        assertEquals(bookingService.getBookingsMadeByUser(0, 5, user.getId(), PAST), List.of(booking1));

        when(bookingRepository.getAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(), any(), any()))
                .thenReturn(List.of(booking1));
        assertNotNull(bookingService.getBookingsMadeByUser(0, 5, user.getId(), CURRENT));
        assertEquals(1, bookingService.getBookingsMadeByUser(0, 5, user.getId(), CURRENT).size());
        assertEquals(bookingService.getBookingsMadeByUser(0, 5, user.getId(), CURRENT), List.of(booking1));

        when(bookingRepository.getAllByBookerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking1));
        assertNotNull(bookingService.getBookingsMadeByUser(0, 5, user.getId(), ALL));
        assertEquals(1, bookingService.getBookingsMadeByUser(0, 5, user.getId(), ALL).size());
        assertEquals(bookingService.getBookingsMadeByUser(0, 5, user.getId(), ALL), List.of(booking1));
    }

    @Test
    void getBookingsForItemsOwned() {
        when(bookingRepository.getAllByItemOwnerIdAndStatus(anyLong(), any(), any()))
                .thenReturn(List.of(booking1));
        assertNotNull(bookingService.getBookingsForItemsOwned(0, 5, user.getId(), WAITING));
        assertEquals(1, bookingService.getBookingsForItemsOwned(0, 5, user.getId(), WAITING).size());
        assertEquals(bookingService.getBookingsForItemsOwned(0, 5, user.getId(), WAITING), List.of(booking1));

        when(bookingRepository.getAllByItemOwnerIdAndStatus(anyLong(), any(), any()))
                .thenReturn(List.of(booking1));
        assertNotNull(bookingService.getBookingsForItemsOwned(0, 5, user.getId(), REJECTED));
        assertEquals(1, bookingService.getBookingsForItemsOwned(0, 5, user.getId(), REJECTED).size());
        assertEquals(bookingService.getBookingsForItemsOwned(0, 5, user.getId(), REJECTED), List.of(booking1));

        when(bookingRepository.getAllByItemOwnerIdAndStartAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking1));
        assertNotNull(bookingService.getBookingsForItemsOwned(0, 5, user.getId(), FUTURE));
        assertEquals(1, bookingService.getBookingsForItemsOwned(0, 5, user.getId(), FUTURE).size());
        assertEquals(bookingService.getBookingsForItemsOwned(0, 5, user.getId(), FUTURE), List.of(booking1));

        when(bookingRepository.getAllByItemOwnerIdAndEndBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking1));
        assertNotNull(bookingService.getBookingsForItemsOwned(0, 5, user.getId(), PAST));
        assertEquals(1, bookingService.getBookingsForItemsOwned(0, 5, user.getId(), PAST).size());
        assertEquals(bookingService.getBookingsForItemsOwned(0, 5, user.getId(), PAST), List.of(booking1));

        when(bookingRepository.getAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(), any(), any(), any()))
                .thenReturn(List.of(booking1));
        assertNotNull(bookingService.getBookingsForItemsOwned(0, 5, user.getId(), CURRENT));
        assertEquals(1, bookingService.getBookingsForItemsOwned(0, 5, user.getId(), CURRENT).size());
        assertEquals(bookingService.getBookingsForItemsOwned(0, 5, user.getId(), CURRENT), List.of(booking1));

        when(bookingRepository.getAllByItemOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking1));
        assertNotNull(bookingService.getBookingsForItemsOwned(0, 5, user.getId(), ALL));
        assertEquals(1, bookingService.getBookingsForItemsOwned(0, 5, user.getId(), ALL).size());
        assertEquals(bookingService.getBookingsForItemsOwned(0, 5, user.getId(), ALL), List.of(booking1));
    }

    @Test
    void getLastBooking() {
        when(bookingRepository.getFirstByItemIdOrderByStart(anyLong())).thenReturn(booking2);
        assertNotNull(bookingService.getLastBooking(item.getId(), user.getId()));
        assertEquals(bookingService.getLastBooking(item.getId(), user.getId()), booking2);
    }

    @Test
    void getNextBooking() {
        when(bookingRepository.getFirstByItemIdOrderByEndDesc(anyLong())).thenReturn(booking1);
        assertNotNull(bookingService.getNextBooking(item.getId(), user.getId()));
        assertEquals(bookingService.getNextBooking(item.getId(), user.getId()), booking1);
    }

    @Test
    void book() {
        when(bookingRepository.save(any())).thenReturn(booking1);
        assertNotNull(bookingService.book(booking1, user.getId()));
        assertEquals(bookingService.book(booking1, user.getId()), booking1);
    }

    @Test
    void updateWithBookingApproved() {
        when(bookingRepository.save(any())).thenReturn(booking1);
        assertNotNull(bookingService.update(booking1, user.getId(), true));
        assertEquals(bookingService.update(booking1, user.getId(), true), booking1);
    }

    @Test
    void updateWithBookingRejected() {
        when(bookingRepository.save(any())).thenReturn(booking1);
        assertNotNull(bookingService.update(booking1, user.getId(), false));
        assertEquals(bookingService.update(booking1, user.getId(), false), booking1);
    }

    @Test
    void delete() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking1));
        assertNotNull(bookingService.findById(booking1.getId(), user.getId()));
        assertEquals(bookingService.findById(booking1.getId(), user.getId()), booking1);
    }

    @Test
    void findById() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking1));
        assertNotNull(bookingService.findById(booking1.getId(), user.getId()));
        assertEquals(bookingService.findById(booking1.getId(), user.getId()), booking1);
    }

    @Test
    void findByIdWithWrongId() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookingService.findById(booking1.getId(), user.getId()));
    }

    @Test
    void isUserPresentAmongBookers() {
        when(bookingRepository.existsByBookerIdAndStatus(anyLong(), any())).thenReturn(true);
        assertTrue(bookingService.isUserPresentAmongBookers(user.getId()));
    }

    @Test
    void findBookingByUserAndItem() {
        when(bookingRepository.getFirstByItemIdAndBookerIdOrderByStart(anyLong(), anyLong())).thenReturn(booking1);
        assertNotNull(bookingService.findBookingByUserAndItem(user.getId(), item.getId()));
        assertEquals(bookingService.findBookingByUserAndItem(user.getId(), item.getId()), booking1);
    }
}