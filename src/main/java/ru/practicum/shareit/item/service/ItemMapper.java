package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest() : null
        );
    }

    public static List<ItemDto> toItemDtoList(List<Item> items) {
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public static Item toItem(ItemDto itemDto, Long userId) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                userId,
                itemDto.getRequest() != null ? itemDto.getRequest() : null
        );
    }
}