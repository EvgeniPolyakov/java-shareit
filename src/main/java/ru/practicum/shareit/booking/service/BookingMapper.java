package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.IncomingBookingDto;
import ru.practicum.shareit.booking.model.OutgoingBookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    private final ItemService itemService;
    private final UserService userService;

    public static OutgoingBookingDto toOutgoingBookingDto(Booking booking) {
        return new OutgoingBookingDto(
                booking.getId(),
                booking.getBooker().getId()
        );
    }

    public Booking toBooking(IncomingBookingDto bookingDto, Long userId) {
        return new Booking(
                null,
                itemService.findById(bookingDto.getItemId()),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                userService.findById(userId),
                Status.WAITING
        );
    }
}