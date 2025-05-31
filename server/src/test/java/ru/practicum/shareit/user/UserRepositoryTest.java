package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@Transactional
@SpringBootTest(
        properties = "jdbc:h2:mem:shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("UserRepository")
public class UserRepositoryTest extends UserInit {

    private final UserRepository userRepository;

    @Test
    @DisplayName("Должен находить пользователя по email")
    void shouldFindUserByEmail() {
        User user = userRepository.save(UserMapper.mapFromRequest(newUserDto));
        User userFromRepository = userRepository.findByEmailLike(userWithId.getEmail())
                .orElseThrow(() -> new NotFoundException());
        Assertions.assertEquals(user, userFromRepository, "Пользователи не совпадают при сохранении в БД");
    }
}
