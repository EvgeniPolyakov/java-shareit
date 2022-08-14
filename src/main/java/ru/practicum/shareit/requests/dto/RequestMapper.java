package ru.practicum.shareit.requests.dto;

import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest request) {
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                request.getRequester(),
                request.getCreated()
        );
    }

    public static List<ItemRequestDto> toItemRequestDtoList(List<ItemRequest> users) {
        return users.stream().map(RequestMapper::toItemRequestDto).collect(Collectors.toList());
    }
}
