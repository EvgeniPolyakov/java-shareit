package ru.practicum.shareit.requests.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.OutgoingItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OutgoingRequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<OutgoingItemDto> items;
}