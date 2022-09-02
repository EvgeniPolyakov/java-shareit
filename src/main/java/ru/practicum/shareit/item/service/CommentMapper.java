package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.IncomingCommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.OutgoingCommentDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    public static OutgoingCommentDto toOutgoingCommentDto(Comment comment) {
        return new OutgoingCommentDto(
                comment.getId(),
                comment.getAuthor().getName(),
                comment.getText(),
                comment.getCreationTime()
        );
    }

    public static List<OutgoingCommentDto> toOutgoingCommentDtoList(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toOutgoingCommentDto).collect(Collectors.toList());
    }

    public static Comment toComment(IncomingCommentDto incomingCommentDto, Item item, User user) {
        return new Comment(
                null,
                item,
                user,
                incomingCommentDto.getText(),
                LocalDateTime.now()
        );
    }
}