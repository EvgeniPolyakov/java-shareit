package ru.practicum.shareit.item.service;

import ru.practicum.shareit.booking.model.GuestBookingDto;
import ru.practicum.shareit.item.model.IncomingItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.OutgoingCommentDto;
import ru.practicum.shareit.item.model.OutgoingItemDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class ItemMapper {

    public static OutgoingItemDto toOutgoingItemDto(
            Item item, GuestBookingDto lastBooking, GuestBookingDto nextBooking, List<OutgoingCommentDto> comments) {
        return new OutgoingItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                comments
        );
    }

    public static Item toItem(IncomingItemDto incomingItemDto, User user) {
        return new Item(
                null,
                incomingItemDto.getName(),
                incomingItemDto.getDescription(),
                incomingItemDto.getAvailable(),
                user,
                null // логика request будет написана позже
        );
    }
}