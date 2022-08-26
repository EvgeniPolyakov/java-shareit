package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.IncomingCommentDto;
import ru.practicum.shareit.item.model.OutgoingCommentDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public CommentMapper(UserService userService, ItemService itemService) {
        this.userService = userService;
        this.itemService = itemService;
    }

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

    public Comment toComment(IncomingCommentDto incomingCommentDto, Long itemId, Long userId) {
        return new Comment(
                null,
                itemService.findById(itemId),
                userService.findById(userId),
                incomingCommentDto.getText(),
                LocalDateTime.now()
        );
    }
}