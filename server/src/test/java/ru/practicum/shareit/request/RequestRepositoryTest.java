package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest(
        properties = "jdbc:h2:mem:shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("RequestRepository")
public class RequestRepositoryTest extends RequestInit {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Test
    @DisplayName("Должен находить запросы по id автора запроса")
    void shouldFindRequestsByRequestorId() {
        User userFromRep = userRepository.save(UserMapper.mapFromRequest(newUserDto));
        Request requestFromRep = requestRepository.save(
                RequestMapper.mapFromRequest(newRequestDto, userFromRep)
        );
        List<Request> requestsFromRepository = requestRepository
                .findAllByRequestor_Id(userFromRep.getId(), Sort.by("id"));
        Assertions.assertEquals(
                List.of(requestFromRep),
                requestsFromRepository,
                "Списки запросов не совпадают при поиске по id автора запроса в БД"
        );
    }

    @Test
    @DisplayName("Должен находить запросы других авторов")
    void shouldFindRequestsOfOtherAuthor() {
        User userFromRep = userRepository.save(UserMapper.mapFromRequest(newUserDto));
        Request requestFromRep = requestRepository.save(
                RequestMapper.mapFromRequest(newRequestDto, userFromRep)
        );
        List<Request> requestsFromRepository = requestRepository
                .findAllByRequestor_IdNot(userFromRep.getId(), Sort.by("id"));
        Assertions.assertEquals(
                new ArrayList<>(),
                requestsFromRepository,
                "Список запросов не пуст при поиске чужих запросов, а должен быть"
        );
    }
}
