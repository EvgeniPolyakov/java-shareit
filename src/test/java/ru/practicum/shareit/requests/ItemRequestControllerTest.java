package ru.practicum.shareit.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.common.ValidationService;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;
    @MockBean
    ItemController itemController;
    @MockBean
    ValidationService validationService;
    @MockBean
    ItemRequestService requestService;

    @Autowired
    private MockMvc mvc;

    private final User user = new User(1L, "userName", "user@email.com");
    private final ItemRequest request = new ItemRequest(
            1L,
            "description",
            user,
            LocalDateTime.of(2000, 11, 11, 11, 11, 11));

    @Test
    void getOwn() throws Exception {
        when(requestService.getOwn(anyLong())).thenReturn(List.of(request));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(request.getDescription())))
                .andExpect(jsonPath("$.[0].created", is("2000-11-11T11:11:11")));
    }

    @Test
    void getAll() throws Exception {
        when(requestService.getAll(anyInt(), anyInt(), anyLong())).thenReturn(List.of(request));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(request.getDescription())))
                .andExpect(jsonPath("$.[0].created", is("2000-11-11T11:11:11")));
    }

    @Test
    void getById() throws Exception {
        when(requestService.findById(anyLong())).thenReturn(request);

        mvc.perform(get("/requests/{id}", request.getId())
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.created", is("2000-11-11T11:11:11")));
    }

    @Test
    void add() throws Exception {
        when(requestService.save(any())).thenReturn(request);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.created", is("2000-11-11T11:11:11")));
    }

    @Test
    void update() throws Exception {
        when(requestService.update(anyLong(), any())).thenReturn(request);

        mvc.perform(patch("/requests/{id}", request.getId())
                        .header("X-Sharer-User-Id", user.getId())
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(request.getDescription())))
                .andExpect(jsonPath("$.created", is("2000-11-11T11:11:11")));
    }
}