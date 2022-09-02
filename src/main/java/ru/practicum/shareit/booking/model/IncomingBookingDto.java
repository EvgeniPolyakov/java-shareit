package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class IncomingBookingDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
