package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QueryParam;

import java.util.List;

public interface BookingService {
    List<Booking> getBookingsMadeByUser(Integer from, Integer size, Long userId, QueryParam queryParam);

    Booking book(Booking booking, Long userId);

    Booking update(Booking booking, Long userId, boolean status);

    Booking findById(Long id, Long userId);

    List<Booking> getBookingsForItemsOwned(Integer from, Integer size, Long userId, QueryParam queryParam);

    Booking getLastBooking(Long itemId, Long userId);

    Booking getNextBooking(Long itemId, Long userId);

    boolean isUserPresentAmongBookers(Long id);

    Booking findBookingByUserAndItem(Long itemId, Long userId);
}
