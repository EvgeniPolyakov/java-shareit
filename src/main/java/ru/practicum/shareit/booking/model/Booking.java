package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;

import java.sql.Date;

@Data
@AllArgsConstructor
public class Booking {
    private Long id;
    private Item item;
    private Date start;
    private Date end;
    private Long booker;
    private Status status;
    private String review;
}
