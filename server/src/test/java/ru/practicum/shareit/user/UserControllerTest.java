package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dao.UserServiceImpl;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@DisplayName("UserController")
public class UserControllerTest extends UserInit {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserServiceImpl userService;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Должен получать пользователей")
    void shouldGetUsers() throws Exception {
        List<UserDto> usersDto = List.of(userDto);

        when(userService.getUsers())
                .thenReturn(usersDto);

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(usersDto)));
    }

    @Test
    @DisplayName("Должен находить пользователя по id")
    void shouldFindUserById() throws Exception {
        when(userService.getUserById(anyLong()))
                .thenReturn(userDto);

        mvc.perform(get("/users/" + userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    @DisplayName("Должен создавать пользователя")
    void shouldCreateUser() throws Exception {
        when(userService.createUser(any()))
                .thenReturn(userDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    @DisplayName("Должен обновлять пользователя по id")
    void shouldUpdateUserById() throws Exception {
        NewUserDto userDtoOnUpdate = NewUserDto.builder()
                .name("Timuchin")
                .build();

        UserDto updatedUserDto = UserDto.builder()
                .id(userId)
                .name("Timuchin")
                .email("drakonhftfg@yandex.ru")
                .build();

        when(userService.updateUser(anyLong(), any()))
                .thenReturn(updatedUserDto);

        mvc.perform(patch("/users/" + userId)
                        .content(mapper.writeValueAsString(userDtoOnUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUserDto.getId()))
                .andExpect(jsonPath("$.name").value(updatedUserDto.getName()))
                .andExpect(jsonPath("$.email").value(updatedUserDto.getEmail()));
    }

    @Test
    @DisplayName("Должен удалять пользователя по id")
    void shouldRemoveUserById() throws Exception {
        mvc.perform(delete("/users/" + userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk());
    }
}
