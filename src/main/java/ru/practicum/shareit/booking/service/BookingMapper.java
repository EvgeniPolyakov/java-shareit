package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getItem(),
                booking.getStart(),
                booking.getEnd()
        );
    }

    public static List<BookingDto> toBookingDtoList(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getItem(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                null, // в будущем - Long booker
                null, // в будущем - Status status,
                null // в будущем - String review
        );
    }
}