package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dao.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
@DisplayName("BookingController")
public class BookingControllerTest extends BookingInit {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingServiceImpl bookingService;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Должен получить бронирования чужих вещей текущим пользователем")
    void shouldGetOutgoingBookings() throws Exception {
        List<BookingDto> bookingsDto = List.of(bookingDto);

        when(bookingService.getBookingsOutgoing(anyLong(), any()))
                .thenReturn(bookingsDto);

        mvc.perform(get("/bookings")
                        .header(headerCurrentUser, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingsDto)));
    }

    @Test
    @DisplayName("Должен получить бронирования вещей текущего пользователя")
    void shouldGetIncomingBookings() throws Exception {
        List<BookingDto> bookingsDto = List.of(bookingDto);

        when(bookingService.getBookingsIncoming(anyLong(), any()))
                .thenReturn(bookingsDto);

        mvc.perform(get("/bookings/owner")
                        .header(headerCurrentUser, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingsDto)));
    }

    @Test
    @DisplayName("Должен находить бронирование по id")
    void shouldFindBookingById() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/" + bookingId)
                        .header(headerCurrentUser, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.item").value(bookingDto.getItem()))
                .andExpect(jsonPath("$.booker").value(bookingDto.getBooker()));

    }

    @Test
    @DisplayName("Должен создавать бронирование")
    void shouldCreateBooking() throws Exception {
        when(bookingService.createBooking(anyLong(), any()))
                .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .header(headerCurrentUser, userId)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.item").value(bookingDto.getItem()))
                .andExpect(jsonPath("$.booker").value(bookingDto.getBooker()));
    }

    @Test
    @DisplayName("Должен обновлять статус бронирования по id")
    void shouldUpdateStatusOfBookingById() throws Exception {
        when(bookingService.updateStatusOfBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/" + bookingId)
                        .header(headerCurrentUser, userId)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.item").value(bookingDto.getItem()))
                .andExpect(jsonPath("$.booker").value(bookingDto.getBooker()));
    }
}
