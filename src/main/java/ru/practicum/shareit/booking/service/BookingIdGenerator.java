package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Component;

@Component
public class BookingIdGenerator {
    private Long bookingBaseId = 0L;

    public long generateBookingId() {
        return ++bookingBaseId;
    }

    public void setBookingBaseId(Long userBaseId) {
        this.bookingBaseId = userBaseId;
    }
}