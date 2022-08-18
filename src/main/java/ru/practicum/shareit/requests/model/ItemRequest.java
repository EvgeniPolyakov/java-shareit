package ru.practicum.shareit.requests.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ItemRequest {
    private Long id;
    private String description;
    private User requester;
    private Instant created;
}
