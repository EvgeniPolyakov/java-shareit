package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingStorage {
    List<Booking> getAll();

    Booking getById(Long id);

    Booking add(Booking booking);

    Booking update(Booking booking);

    void delete(Long id);
}
