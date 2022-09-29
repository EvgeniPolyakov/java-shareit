package ru.practicum.shareit.requests.service;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.OutgoingItemDto;
import ru.practicum.shareit.item.service.CommentMapper;
import ru.practicum.shareit.requests.model.IncomingRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.OutgoingRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemRequestMapperTest {
    private final User user = new User(1L, "user", "user@email.com");

    @Test
    void toOutgoingRequestDto() {
        ItemRequest request = new ItemRequest(1L, "description1", user, LocalDateTime.now());
        Item item1 = new Item(1L, "item1", "text1", true, user, request);
        Item item2 = new Item(2L, "item2", "text2", true, user, request);
        Comment comment = new Comment(1L, item1, user, "text", LocalDateTime.now());
        Booking booking1 = new Booking(
                1L,
                item1,
                LocalDateTime.now().plusMinutes(1),
                LocalDateTime.now().plusMinutes(2),
                user,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                3L,
                item2,
                LocalDateTime.now().minusMinutes(2),
                LocalDateTime.now().minusMinutes(1),
                user,
                Status.APPROVED
        );
        OutgoingItemDto item1dto = new OutgoingItemDto(
                1L,
                "item1",
                "text1",
                true,
                BookingMapper.toGuestBookingDto(booking1),
                BookingMapper.toGuestBookingDto(booking1),
                List.of(CommentMapper.toOutgoingCommentDto(comment)),
                request.getId());
        OutgoingItemDto item2dto = new OutgoingItemDto(
                2L,
                "item2",
                "text2",
                true,
                BookingMapper.toGuestBookingDto(booking2),
                BookingMapper.toGuestBookingDto(booking2),
                List.of(CommentMapper.toOutgoingCommentDto(comment)),
                request.getId());
        OutgoingRequestDto requestDto = ItemRequestMapper.toOutgoingRequestDto(request, List.of(item1dto, item2dto));

        assertNotNull(requestDto);
        assertThat(requestDto.getId()).isEqualTo(request.getId());
        assertThat(requestDto.getDescription()).isEqualTo(request.getDescription());
        assertThat(requestDto.getItems()).isEqualTo(List.of(item1dto, item2dto));
    }

    @Test
    void toItemRequest() {
        IncomingRequestDto requestDto = new IncomingRequestDto("description");
        ItemRequest request = ItemRequestMapper.toItemRequest(requestDto, user);

        assertNotNull(request);
        assertThat(request.getDescription()).isEqualTo(requestDto.getDescription());
        assertThat(request.getRequester()).isEqualTo(user);
    }
}