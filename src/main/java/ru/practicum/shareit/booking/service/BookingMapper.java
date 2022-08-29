package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.IncomingBookingDto;
import ru.practicum.shareit.booking.model.OutgoingBookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@RequiredArgsConstructor
public class BookingMapper {

    public static OutgoingBookingDto toOutgoingBookingDto(Booking booking) {
        return booking != null ? new OutgoingBookingDto(booking.getId(), booking.getBooker().getId()) : null;
    }

    public static Booking toBooking(IncomingBookingDto bookingDto, Item item, User user) {
        return new Booking(
                null,
                item,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                user,
                Status.WAITING
        );
    }
}