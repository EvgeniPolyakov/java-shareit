package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;

import java.sql.Date;

@Data
@AllArgsConstructor
public class BookingDto {
    private long id;
    private Item item;
    private Date start;
    private Date end;
    private Long booker;
    private Status status;
    private String review;
}
