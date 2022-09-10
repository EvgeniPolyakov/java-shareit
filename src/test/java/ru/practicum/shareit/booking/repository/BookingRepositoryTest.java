package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    BookingRepository bookingRepository;

    private User user1;
    private User user2;
    private Item item1;
    private Item item2;
    private Item item3;
    private Booking booking1;
    private Booking booking2;
    private Booking booking3;
    private Booking booking4;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User(1L, "user1", "user1@email.com"));
        user2 = userRepository.save(new User(2L, "user2", "user2@email.com"));
        item1 = itemRepository.save(new Item(1L, "item1", "text1", true, user1, null));
        item2 = itemRepository.save(new Item(2L, "item2", "text2", true, user2, null));
        item3 = itemRepository.save(new Item(3L, "item3", "text3", true, user1, null));
        booking1 = bookingRepository.save(new Booking(
                1L,
                item2,
                LocalDateTime.now().minusMinutes(2),
                LocalDateTime.now().minusMinutes(1),
                user1,
                Status.WAITING));
        booking2 = bookingRepository.save(new Booking(
                2L,
                item1,
                LocalDateTime.now().plusMinutes(1),
                LocalDateTime.now().plusMinutes(2),
                user2,
                Status.APPROVED));
        booking3 = bookingRepository.save(new Booking(
                3L,
                item2,
                LocalDateTime.now().plusMinutes(3),
                LocalDateTime.now().plusMinutes(4),
                user1,
                Status.APPROVED));
        booking4 = bookingRepository.save(new Booking(
                2L,
                item3,
                LocalDateTime.now().minusMinutes(1),
                LocalDateTime.now().plusMinutes(1),
                user2,
                Status.APPROVED));
    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    void getAllByBookerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.getAllByBookerIdOrderByStartDesc(user1.getId(), Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertEquals(bookings.get(0), booking3);
        assertEquals(bookings.get(1), booking1);
    }

    @Test
    void getAllByBookerIdAndStatus() {
        List<Booking> bookings = bookingRepository.getAllByBookerIdAndStatus(user1.getId(), Status.WAITING, Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0), booking1);
    }

    @Test
    void getAllByBookerIdAndEndBeforeOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.getAllByBookerIdAndEndBeforeOrderByStartDesc(
                user1.getId(),
                LocalDateTime.now(),
                Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0), booking1);
    }

    @Test
    void getAllByBookerIdAndStartAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.getAllByBookerIdAndStartAfterOrderByStartDesc(
                user1.getId(),
                LocalDateTime.now(),
                Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0), booking3);
    }

    @Test
    void getAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.getAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                user2.getId(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0), booking4);
    }

    @Test
    void getAllByItemOwnerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.getAllByItemOwnerIdOrderByStartDesc(user2.getId(), Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertEquals(bookings.get(0), booking3);
        assertEquals(bookings.get(1), booking1);
    }

    @Test
    void getAllByItemOwnerIdAndStatus() {
        List<Booking> bookings = bookingRepository.getAllByItemOwnerIdAndStatus(user2.getId(), Status.WAITING, Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0), booking1);
    }

    @Test
    void getAllByItemOwnerIdAndEndBeforeOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.getAllByItemOwnerIdAndEndBeforeOrderByStartDesc(
                user2.getId(),
                LocalDateTime.now(),
                Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0), booking1);
    }

    @Test
    void getAllByItemOwnerIdAndStartAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.getAllByItemOwnerIdAndStartAfterOrderByStartDesc(
                user2.getId(),
                LocalDateTime.now(),
                Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0), booking3);
    }

    @Test
    void getAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.getAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                user1.getId(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                Pageable.unpaged());
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings.get(0), booking4);
    }

    @Test
    void getFirstByItemIdOrderByStart() {
        Booking booking = bookingRepository.getFirstByItemIdOrderByStart(item2.getId());
        assertEquals(booking, booking1);
    }

    @Test
    void getFirstByItemIdOrderByEndDesc() {
        Booking booking = bookingRepository.getFirstByItemIdOrderByEndDesc(item2.getId());
        assertEquals(booking, booking3);
    }

    @Test
    void getFirstByItemIdAndBookerIdOrderByStart() {
        Booking booking = bookingRepository.getFirstByItemIdAndBookerIdOrderByStart(item1.getId(), user2.getId());
        assertEquals(booking, booking2);
    }

    @Test
    void existsByBookerIdAndStatus() {
        assertTrue(bookingRepository.existsByBookerIdAndStatus(user1.getId(), Status.WAITING));
        assertFalse(bookingRepository.existsByBookerIdAndStatus(user1.getId(), Status.REJECTED));
        assertTrue(bookingRepository.existsByBookerIdAndStatus(user2.getId(), Status.APPROVED));
    }
}