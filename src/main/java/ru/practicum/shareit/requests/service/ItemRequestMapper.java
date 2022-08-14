package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.ItemRequestDto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest request) {
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                request.getRequester()
        );
    }

    public static List<ItemRequestDto> toItemRequestDtoList(List<ItemRequest> users) {
        return users.stream().map(ItemRequestMapper::toItemRequestDto).collect(Collectors.toList());
    }

    public static ItemRequest toItemRequest(ItemRequestDto requestDto) {
        return new ItemRequest(
                requestDto.getId(),
                requestDto.getDescription(),
                requestDto.getRequester(),
                Instant.now()
        );
    }
}
