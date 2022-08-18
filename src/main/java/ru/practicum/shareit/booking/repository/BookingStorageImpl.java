package ru.practicum.shareit.booking.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingIdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookingStorageImpl implements BookingStorage {
    private final Map<Long, Booking> bookings = new HashMap<>();
    private final BookingIdGenerator idGenerator;

    @Autowired
    public BookingStorageImpl(BookingIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    @Override
    public List<Booking> getAll() {
        return new ArrayList<>(bookings.values());
    }

    @Override
    public Booking getById(Long id) {
        return bookings.get(id);
    }

    @Override
    public Booking add(Booking booking) {
        generateId(booking);
        bookings.put(booking.getId(), booking);
        return bookings.get(booking.getId());
    }

    @Override
    public Booking update(Booking booking) {
        bookings.put(booking.getId(), booking);
        return bookings.get(booking.getId());
    }

    @Override
    public void delete(Long id) {
        bookings.remove(id);
    }

    private void generateId(Booking booking) {
        Long newId = idGenerator.generateBookingId();
        booking.setId(newId);
    }
}
