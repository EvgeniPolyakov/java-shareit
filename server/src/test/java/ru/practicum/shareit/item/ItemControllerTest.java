package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.GuestBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.ValidationService;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.OutgoingItemDto;
import ru.practicum.shareit.item.service.CommentMapper;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;
    @MockBean
    UserService userService;
    @MockBean
    BookingService bookingService;
    @MockBean
    ValidationService validationService;
    @MockBean
    ItemRequestService requestService;

    @Autowired
    private MockMvc mvc;

    private final User user = new User(1L, "userName", "user@email.com");
    private final ItemRequest request = new ItemRequest(1L, "description", user, LocalDateTime.now());
    private final Item item1 = new Item(1L, "itemName", "text", true, user, null);
    private final Item item2 = new Item(2L, "itemName2", "text2", true, user, request);
    private final GuestBookingDto lastBooking = new GuestBookingDto(1L, user.getId());
    private final GuestBookingDto nextBooking = new GuestBookingDto(2L, user.getId());
    private final Comment comment = new Comment(
            1L,
            item2,
            user,
            "comment",
            LocalDateTime.of(2000, 11, 11, 11, 11, 11));
    private final OutgoingItemDto oiDto = new OutgoingItemDto(
            1L,
            "itemName",
            "text",
            true,
            lastBooking,
            nextBooking,
            List.of(CommentMapper.toOutgoingCommentDto(comment)),
            request.getId()
    );

    @Test
    void getAll() throws Exception {
        when(itemService.getItemsByUserId(anyInt(), anyInt(), anyLong())).thenReturn(List.of(item1, item2));
        when(itemService.findById(item1.getId())).thenReturn(item1);
        when(itemService.findById(item2.getId())).thenReturn(item2);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(item1.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(item1.getName())))
                .andExpect(jsonPath("$.[0].description", is(item1.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(item1.getAvailable())))
                .andExpect(jsonPath("$.[1].id", is(item2.getId()), Long.class))
                .andExpect(jsonPath("$.[1].name", is(item2.getName())))
                .andExpect(jsonPath("$.[1].description", is(item2.getDescription())))
                .andExpect(jsonPath("$.[1].available", is(item2.getAvailable())));
    }

    @Test
    void getItem() throws Exception {
        when(itemService.findById(anyLong())).thenReturn(item1);
        when(itemService.findById(item1.getId())).thenReturn(item1);

        mvc.perform(get("/items/{id}", item1.getId())
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item1.getName())))
                .andExpect(jsonPath("$.description", is(item1.getDescription())))
                .andExpect(jsonPath("$.available", is(item1.getAvailable())));
    }

    @Test
    void addItem() throws Exception {
        when(itemService.save(any())).thenReturn(item1);
        when(itemService.findById(item1.getId())).thenReturn(item1);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(item1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item1.getName())))
                .andExpect(jsonPath("$.description", is(item1.getDescription())))
                .andExpect(jsonPath("$.available", is(item1.getAvailable())));
    }

    @Test
    void addComment() throws Exception {
        when(itemService.saveComment(any(), anyLong())).thenReturn(comment);
        when(userService.findById(user.getId())).thenReturn(user);

        mvc.perform(post("/items/{id}/comment", item1.getId())
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(comment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.authorName", is(comment.getAuthor().getName())))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.creationTime", is("2000-11-11T11:11:11")));
    }

    @Test
    void update() throws Exception {
        when(itemService.update(anyLong(), anyLong(), any())).thenReturn(item1);
        when(itemService.findById(item1.getId())).thenReturn(item1);
        MockedStatic<ItemMapper> mockStatic = mockStatic(ItemMapper.class);
        mockStatic.when(() -> ItemMapper.toItem(any(), any())).thenReturn(item1);
        mockStatic.when(() -> ItemMapper.toOutgoingItemDto(any(), any(), any(), any())).thenReturn(oiDto);

        mvc.perform(patch("/items/{id}", item1.getId())
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(item1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item1.getName())))
                .andExpect(jsonPath("$.description", is(item1.getDescription())))
                .andExpect(jsonPath("$.available", is(item1.getAvailable())));
    }

    @Test
    void search() throws Exception {
        when(itemService.search(anyInt(), anyInt(), anyString())).thenReturn(List.of(item1));
        when(itemService.findById(item1.getId())).thenReturn(item1);

        mvc.perform(get("/items/search")
                        .param("text", "search query")
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(item1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(item1.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(item1.getName())))
                .andExpect(jsonPath("$.[0].description", is(item1.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(item1.getAvailable())));
    }

    @Test
    void searchWithEmptyString() throws Exception {
        when(itemService.search(anyInt(), anyInt(), anyString())).thenReturn(Collections.emptyList());
        when(itemService.findById(item1.getId())).thenReturn(item1);

        mvc.perform(get("/items/search")
                        .param("text", " ")
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(item1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void remove() throws Exception {
        mvc.perform(delete("/items/{id}", item1.getId())
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk());
        verify(itemService, times(1)).delete(user.getId(), item1.getId());
    }

    @Test
    void getAllByRequestId() {
        when(itemService.getAllByRequestId(request.getId())).thenReturn(List.of(item1, item2));
        itemService.getAllByRequestId(request.getId());
        verify(itemService, times(1)).getAllByRequestId(request.getId());
    }
}