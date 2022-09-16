package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.item.model.OutgoingItemDto;
import ru.practicum.shareit.requests.model.IncomingRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.OutgoingRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {

    public static OutgoingRequestDto toOutgoingRequestDto(ItemRequest request, List<OutgoingItemDto> items) {
        return new OutgoingRequestDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                items
        );
    }

    public static ItemRequest toItemRequest(IncomingRequestDto requestDto, User user) {
        return new ItemRequest(
                null,
                requestDto.getDescription(),
                user,
                LocalDateTime.now()
        );
    }
}
