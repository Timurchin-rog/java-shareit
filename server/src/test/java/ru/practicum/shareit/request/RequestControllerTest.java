package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dao.RequestServiceImpl;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestLongDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RequestController.class)
@DisplayName("RequestController")
public class RequestControllerTest extends RequestInit {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    RequestServiceImpl requestService;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Должен получить запросы текущего пользователя")
    void shouldGetRequestsOfCurrentUser() throws Exception {
        List<RequestLongDto> requestsDto = List.of(requestLong);

        when(requestService.getMyRequests(anyLong()))
                .thenReturn(requestsDto);

        mvc.perform(get("/requests")
                        .header(headerCurrentUser, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestsDto)));
    }

    @Test
    @DisplayName("Должен получить запросы других пользователей")
    void shouldGetRequestsOfOtherUser() throws Exception {
        List<RequestDto> requestsDto = List.of(requestDto);

        when(requestService.getOtherRequests(anyLong()))
                .thenReturn(requestsDto);

        mvc.perform(get("/requests/all")
                        .header(headerCurrentUser, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestsDto)));
    }

    @Test
    @DisplayName("Должен находить запрос по id")
    void shouldFindRequestById() throws Exception {
        when(requestService.getRequestById(anyLong()))
                .thenReturn(requestLong);

        mvc.perform(get("/requests/" + requestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestLong.getId()))
                .andExpect(jsonPath("$.description").value(requestLong.getDescription()))
                .andExpect(jsonPath("$.requestor").value(requestLong.getRequestor()));
    }

    @Test
    @DisplayName("Должен создавать запрос")
    void shouldCreateRequest() throws Exception {
        when(requestService.createRequest(anyLong(), any()))
                .thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .header(headerCurrentUser, userId)
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestDto.getId()))
                .andExpect(jsonPath("$.description").value(requestDto.getDescription()))
                .andExpect(jsonPath("$.requestor").value(requestDto.getRequestor()));
    }
}
