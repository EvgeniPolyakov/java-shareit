package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.model.GuestBookingDto;

import java.util.List;

@Data
@AllArgsConstructor
public class OutgoingItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private GuestBookingDto lastBooking;
    private GuestBookingDto nextBooking;
    private List<OutgoingCommentDto> comments;
    private Long requestId;
}