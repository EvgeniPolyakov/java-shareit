package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    List<Booking> getBookingsMadeByUser(Long userId, String state);

    Booking book(Booking booking, Long userId);

    Booking update(Booking booking, Long userId, boolean status);

    void delete(Booking booking);

    Booking findById(Long id, Long userId);

    List<Booking> getBookingsForItemsOwned(Long userId, String state);

    Booking getLastBooking(Long itemId, Long userId);

    Booking getNextBooking(Long itemId, Long userId);

    boolean isUserPresentAmongBookers(Long id);

    Booking findBookingByUserAndItem(Long itemId, Long userId);
}
