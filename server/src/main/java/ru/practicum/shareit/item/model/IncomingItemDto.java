package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class IncomingItemDto {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}