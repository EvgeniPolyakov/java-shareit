package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.GuestBookingDto;
import ru.practicum.shareit.item.model.IncomingItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.OutgoingCommentDto;
import ru.practicum.shareit.item.model.OutgoingItemDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemMapperTest {
    private final User user = new User(1L, "userName", "user@email.com");

    @Test
    void toOutgoingItemDto() {
        ItemRequest request = new ItemRequest(1L, "description1", user, LocalDateTime.now());
        Item item = new Item(1L, "itemName", "text", true, user, request);
        GuestBookingDto lastBooking = new GuestBookingDto(1L, 1L);
        GuestBookingDto nextBooking = new GuestBookingDto(2L, 1L);
        LocalDateTime created = LocalDateTime.of(2000, 11, 11, 11, 11, 11);
        OutgoingCommentDto commentDto = new OutgoingCommentDto(1L, user.getName(), "comment", created);
        OutgoingItemDto oiDto = ItemMapper.toOutgoingItemDto(
                item,
                lastBooking,
                nextBooking,
                List.of(commentDto)
        );

        assertNotNull(oiDto);
        assertThat(oiDto.getId()).isEqualTo(item.getId());
        assertThat(oiDto.getName()).isEqualTo(item.getName());
        assertThat(oiDto.getAvailable()).isEqualTo(item.getAvailable());
        assertThat(oiDto.getDescription()).isEqualTo(item.getDescription());
        assertThat(oiDto.getComments()).isEqualTo(List.of(commentDto));
        assertThat(oiDto.getLastBooking()).isEqualTo(lastBooking);
        assertThat(oiDto.getNextBooking()).isEqualTo((nextBooking));
        assertThat(oiDto.getRequestId()).isEqualTo(item.getRequest().getId());
    }

    @Test
    void toItem() {
        IncomingItemDto itemDto = new IncomingItemDto("itemName", "text", true, 1L);
        Item itemToTest = ItemMapper.toItem(itemDto, user);

        assertNotNull(itemToTest);
        assertThat(itemToTest.getName()).isEqualTo(itemDto.getName());
        assertThat(itemToTest.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(itemToTest.getAvailable()).isEqualTo(itemDto.getAvailable());
        assertThat(itemToTest.getOwner()).isEqualTo(user);
    }
}