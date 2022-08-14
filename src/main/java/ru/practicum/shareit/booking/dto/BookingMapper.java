package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getItem(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker(),
                booking.getStatus(),
                booking.getReview()
        );
    }

    public static List<BookingDto> toBookingDtoList(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }
}
