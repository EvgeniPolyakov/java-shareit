package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GuestBookingDto {
    private Long id;
    private Long bookerId;
}
