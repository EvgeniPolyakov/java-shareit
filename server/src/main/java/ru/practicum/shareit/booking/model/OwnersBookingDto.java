package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OwnersBookingDto {
    private Long id;
    private User booker;
    private Status status;
    private Item item;
    private String itemName;
    private LocalDateTime start;
    private LocalDateTime end;
}
