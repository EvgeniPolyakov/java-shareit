package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class OutgoingCommentDto {
    private Long id;
    private String authorName;
    private String text;
    private LocalDateTime creationTime;
}
