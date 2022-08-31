package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BookingMapper {

    public static GuestBookingDto toGuestBookingDto(Booking booking) {
        return booking != null ? new GuestBookingDto(booking.getId(), booking.getBooker().getId()) : null;
    }

    public static OwnersBookingDto toOwnersBookingDto(Booking booking) {
        return booking != null ? new OwnersBookingDto(
                booking.getId(),
                booking.getBooker(),
                booking.getStatus(),
                booking.getItem(),
                booking.getItem().getName(),
                booking.getStart(),
                booking.getEnd()
        ) : null;
    }

    public static List<OwnersBookingDto> toOwnersBookingDtoList(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::toOwnersBookingDto).collect(Collectors.toList());
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