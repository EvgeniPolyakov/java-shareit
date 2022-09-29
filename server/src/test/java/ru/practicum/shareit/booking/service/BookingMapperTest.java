package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BookingMapperTest {
    private final User user = new User(1L, "user", "user@email.com");
    private final ItemRequest request = new ItemRequest(1L, "description1", user, LocalDateTime.now());
    private final Item item = new Item(1L, "item", "text", true, user, request);
    private final LocalDateTime start = LocalDateTime.now();
    private final LocalDateTime end = LocalDateTime.now();
    private final Booking booking = new Booking(1L, item, start, end, user, Status.WAITING);

    @Test
    void toGuestBookingDto() {
        GuestBookingDto bookingDto = BookingMapper.toGuestBookingDto(booking);

        assertNotNull(bookingDto);
        assertThat(bookingDto.getId()).isEqualTo(booking.getId());
        assertThat(bookingDto.getBookerId()).isEqualTo(booking.getBooker().getId());
    }

    @Test
    void toOwnersBookingDto() {
        OwnersBookingDto bookingDto = BookingMapper.toOwnersBookingDto(booking);

        assertNotNull(bookingDto);
        assertThat(bookingDto.getId()).isEqualTo(booking.getId());
        assertThat(bookingDto.getBooker().getId()).isEqualTo(booking.getBooker().getId());
        assertThat(bookingDto.getBooker().getName()).isEqualTo(booking.getBooker().getName());
        assertThat(bookingDto.getBooker().getEmail()).isEqualTo(booking.getBooker().getEmail());
        assertThat(bookingDto.getStatus()).isEqualTo(booking.getStatus());
        assertThat(bookingDto.getItem().getId()).isEqualTo(booking.getItem().getId());
        assertThat(bookingDto.getItem().getName()).isEqualTo(booking.getItem().getName());
        assertThat(bookingDto.getItemName()).isEqualTo(booking.getItem().getName());
        assertThat(bookingDto.getItemName()).isEqualTo(booking.getItem().getName());
        assertThat(bookingDto.getStart()).isEqualTo(start);
        assertThat(bookingDto.getEnd()).isEqualTo(end);
    }

    @Test
    void toOwnersBookingDtoList() {
        List<OwnersBookingDto> bookings = BookingMapper.toOwnersBookingDtoList(List.of(booking));

        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(bookings, List.of(BookingMapper.toOwnersBookingDto(booking)));

    }

    @Test
    void toBooking() {
        IncomingBookingDto bookingDto = new IncomingBookingDto(item.getId(), start, end);
        Booking bookingToTest = BookingMapper.toBooking(bookingDto, item, user);

        assertNotNull(bookingToTest);
        assertThat(bookingToTest.getItem()).isEqualTo(item);
        assertThat(bookingToTest.getBooker()).isEqualTo(user);
        assertThat(bookingToTest.getStart()).isEqualTo(start);
        assertThat(bookingToTest.getEnd()).isEqualTo(end);
        assertThat(bookingToTest.getStatus()).isEqualTo(Status.WAITING);
    }
}