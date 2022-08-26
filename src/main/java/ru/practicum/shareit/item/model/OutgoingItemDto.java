package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.OutgoingBookingDto;

import java.util.List;

@Data
@AllArgsConstructor
public class OutgoingItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private OutgoingBookingDto lastBooking;
    private OutgoingBookingDto nextBooking;
    private List<OutgoingCommentDto> comments;
}