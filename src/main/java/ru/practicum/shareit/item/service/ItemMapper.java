package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.IncomingItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.OutgoingItemDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    @Autowired
    public ItemMapper(UserService userService, ItemService itemService, BookingService bookingService) {
        this.userService = userService;
        this.itemService = itemService;
        this.bookingService = bookingService;
    }

    public OutgoingItemDto toOutgoingItemDto(Item item, Long userId) {
        return new OutgoingItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                bookingService.getLastBooking(item.getId(), userId) != null ?
                        BookingMapper.toOutgoingBookingDto(bookingService.getLastBooking(item.getId(), userId)) : null,
                bookingService.getNextBooking(item.getId(), userId) != null ?
                        BookingMapper.toOutgoingBookingDto(bookingService.getNextBooking(item.getId(), userId)) : null,
                CommentMapper.toOutgoingCommentDtoList(itemService.findCommentsById(item.getId()))
        );
    }

    public List<OutgoingItemDto> toOutgoingItemDtoList(List<Item> items, Long userId) {
        return items.stream().map((Item item) -> toOutgoingItemDto(item, userId)).collect(Collectors.toList());
    }

    public Item toItem(IncomingItemDto incomingItemDto, Long userId) {
        return new Item(
                null,
                incomingItemDto.getName(),
                incomingItemDto.getDescription(),
                incomingItemDto.getAvailable(),
                userService.findById(userId),
                null // логика request будет написана позже
        );
    }
}