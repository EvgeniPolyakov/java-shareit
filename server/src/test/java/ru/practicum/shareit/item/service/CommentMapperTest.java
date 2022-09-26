package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.IncomingCommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.OutgoingCommentDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CommentMapperTest {
    private final User user = new User(1L, "user", "user@email.com");
    private final ItemRequest request = new ItemRequest(1L, "description1", user, LocalDateTime.now());
    private final Item item = new Item(1L, "item", "text", true, user, request);
    private final LocalDateTime creationTime = LocalDateTime.now();
    private final Comment comment = new Comment(1L, item, user, "text", creationTime);

    @Test
    void toOutgoingCommentDto() {
        OutgoingCommentDto commentDto = CommentMapper.toOutgoingCommentDto(comment);

        assertNotNull(commentDto);
        assertThat(commentDto.getId()).isEqualTo(comment.getId());
        assertThat(commentDto.getText()).isEqualTo(comment.getText());
        assertThat(commentDto.getAuthorName()).isEqualTo(comment.getAuthor().getName());
        assertThat(commentDto.getCreationTime()).isEqualTo(creationTime);
    }

    @Test
    void toOutgoingCommentDtoList() {
        List<OutgoingCommentDto> comments = CommentMapper.toOutgoingCommentDtoList(List.of(comment));

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals(comments, List.of(CommentMapper.toOutgoingCommentDto(comment)));
    }

    @Test
    void toComment() {
        IncomingCommentDto commentDto = new IncomingCommentDto(user, "text");
        Comment commentToTest = CommentMapper.toComment(commentDto, item, user);

        assertNotNull(commentDto);
        assertThat(commentToTest.getAuthor()).isEqualTo(commentDto.getAuthor());
        assertThat(commentToTest.getText()).isEqualTo(commentDto.getText());
        assertThat(commentToTest.getItem()).isEqualTo(item);
    }
}