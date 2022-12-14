package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.model.User;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class IncomingCommentDto {
    private User author;
    private String text;
}
