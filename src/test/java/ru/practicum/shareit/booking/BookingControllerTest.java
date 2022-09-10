package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.ValidationService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;
    @MockBean
    UserService userService;
    @MockBean
    ValidationService validationService;
    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private final User user = new User(1L, "userName", "user@email.com");
    private final Item item = new Item(1L, "itemName", "description", true, user, null);
    private final Booking booking = new Booking(
            1L,
            item,
            LocalDateTime.of(2222, 11, 11, 11, 11, 11),
            LocalDateTime.of(2222, 11, 11, 11, 11, 22),
            user,
            Status.WAITING
    );

    @Test
    void getAllByBooker() throws Exception {
        when(bookingService.getBookingsMadeByUser(anyInt(), anyInt(), anyLong(), any())).thenReturn(List.of(booking));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.[0].booker.id", is(booking.getBooker().getId().intValue())))
                .andExpect(jsonPath("$.[0].status", is(booking.getStatus().name())))
                .andExpect(jsonPath("$.[0].item.id", is(booking.getItem().getId().intValue())))
                .andExpect(jsonPath("$.[0].item.name", is(booking.getItem().getName())))
                .andExpect(jsonPath("$.[0].start", is("2222-11-11T11:11:11")))
                .andExpect(jsonPath("$.[0].end", is("2222-11-11T11:11:22")));
    }

    @Test
    void getAllForItemsOwned() throws Exception {
        when(bookingService.getBookingsForItemsOwned(anyInt(), anyInt(), anyLong(), any())).thenReturn(List.of(booking));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.[0].booker.id", is(booking.getBooker().getId().intValue())))
                .andExpect(jsonPath("$.[0].status", is(booking.getStatus().name())))
                .andExpect(jsonPath("$.[0].item.id", is(booking.getItem().getId().intValue())))
                .andExpect(jsonPath("$.[0].item.name", is(booking.getItem().getName())))
                .andExpect(jsonPath("$.[0].start", is("2222-11-11T11:11:11")))
                .andExpect(jsonPath("$.[0].end", is("2222-11-11T11:11:22")));
    }

    @Test
    void findById() throws Exception {
        when(bookingService.findById(anyLong(), anyLong())).thenReturn(booking);

        mvc.perform(get("/bookings/{id}", booking.getId())
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId().intValue())))
                .andExpect(jsonPath("$.status", is(booking.getStatus().name())))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId().intValue())))
                .andExpect(jsonPath("$.item.name", is(booking.getItem().getName())))
                .andExpect(jsonPath("$.start", is("2222-11-11T11:11:11")))
                .andExpect(jsonPath("$.end", is("2222-11-11T11:11:22")));
    }

    @Test
    void add() throws Exception {
        when(bookingService.book(any(), anyLong())).thenReturn(booking);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.bookerId", is(booking.getBooker().getId().intValue())));
    }

    @Test
    void update() throws Exception {
        when(bookingService.update(any(), anyLong(), anyBoolean())).thenReturn(booking);

        mvc.perform(patch("/bookings/{id}", booking.getId())
                        .header("X-Sharer-User-Id", user.getId())
                        .param("approved", "true")
                        .content(mapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId().intValue())))
                .andExpect(jsonPath("$.status", is(booking.getStatus().name())))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId().intValue())))
                .andExpect(jsonPath("$.item.name", is(booking.getItem().getName())))
                .andExpect(jsonPath("$.start", is("2222-11-11T11:11:11")))
                .andExpect(jsonPath("$.end", is("2222-11-11T11:11:22")));
    }
}