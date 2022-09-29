package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mvc;

    private final User user = new User(1L, "userName", "user@email.com");
    private final User user2 = new User(2L, "userName2", "user2@email.com");

    @Test
    void getAll() throws Exception {
        when(userService.getAll()).thenReturn(List.of(user, user2));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(user.getName())))
                .andExpect(jsonPath("$.[0].email", is(user.getEmail())))
                .andExpect(jsonPath("$.[1].id", is(user2.getId()), Long.class))
                .andExpect(jsonPath("$.[1].name", is(user2.getName())))
                .andExpect(jsonPath("$.[1].email", is(user2.getEmail())));
    }

    @Test
    void add() throws Exception {
        when(userService.save(any())).thenReturn(user);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void update() throws Exception {
        when(userService.update(anyLong(), any())).thenReturn(user);

        mvc.perform(patch("/users/{id}", user.getId())
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void getById() throws Exception {
        when(userService.findById(anyLong())).thenReturn(user);

        mvc.perform(get("/users/{id}", user.getId())
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void remove() throws Exception {
        mvc.perform(delete("/users/{id}", user.getId()))
                .andExpect(status().isOk());
        verify(userService, times(1)).delete(user.getId());
    }
}