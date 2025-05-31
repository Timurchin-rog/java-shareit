package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.dao.RequestServiceImpl;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestLongDto;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
@DisplayName("RequestService")
public class RequestServiceTest extends RequestInit {

    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    RequestRepository requestRepository;

    RequestServiceImpl requestService;

    @BeforeEach
    void init() {
        requestService = new RequestServiceImpl(
                requestRepository,
                userRepository,
                itemRepository
        );
    }

    @Test
    @DisplayName("Должен получить запросы текущего пользователя")
    void shouldGetRequestsOfCurrentUser() {
        Mockito
                .when(requestRepository.findAllByRequestor_Id(anyLong(), any()))
                .thenReturn(List.of(requestWithId));

        Mockito
                .when(itemRepository.findAllByRequest_Id(anyLong()))
                .thenReturn(List.of(itemWithId));

        List<RequestLongDto> requestsDtoFromService = requestService.getMyRequests(userId);

        Assertions.assertEquals(
                List.of(requestLong),
                requestsDtoFromService,
                "Списки запросов пользователя не совпадают при получении");
    }

    @Test
    @DisplayName("Должен получить запросы других пользователей")
    void shouldGetRequestsOfOtherUsers() {
        Mockito
                .when(requestRepository.findAllByRequestor_IdNot(anyLong(), any()))
                .thenReturn(List.of(requestWithId));

        List<RequestDto> requestsDtoFromService = requestService.getOtherRequests(userId);

        Assertions.assertEquals(
                List.of(requestDto),
                requestsDtoFromService,
                "Списки исходящих бронирований не совпадают при получении");
    }

    @Test
    @DisplayName("Должен находить запрос по id")
    void shouldFindItemsByText() {
        Mockito
                .when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(requestWithId));

        Mockito
                .when(itemRepository.findAllByRequest_Id(anyLong()))
                .thenReturn(List.of(itemWithId));

        RequestLongDto requestsDtoFromService = requestService.getRequestById(requestId);

        Assertions.assertEquals(
                requestLong,
                requestsDtoFromService,
                "Запросы не совпадают при поиске по id"
        );
    }

    @Test
    @DisplayName("Должен создавать запрос")
    void shouldCreateBooking() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(userWithId));

        Mockito
                .when(requestRepository.save(any()))
                .thenReturn(requestWithId);

        RequestDto requestFromService = requestService.createRequest(requestId, newRequestDto);
        Assertions.assertEquals(requestDto, requestFromService, "Запросы не совпадают при создании");
    }
}
