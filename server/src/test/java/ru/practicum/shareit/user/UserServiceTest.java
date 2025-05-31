package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dao.UserServiceImpl;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService")
public class UserServiceTest extends UserInit {

    @Mock
    UserRepository userRepository;

    UserServiceImpl userService;

    @BeforeEach
    void init() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    @DisplayName("Должен получать пользователей")
    void shouldGetUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(userWithId));

        List<UserDto> usersDtoFromMethod = userService.getUsers();
        List<UserDto> usersDto = List.of(userDto);

        Assertions.assertEquals(usersDto, usersDtoFromMethod, "Списки пользователей не совпадают при получении");
    }

    @Test
    @DisplayName("Должен находить пользователя по id")
    void shouldFindUserById() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(userWithId));

        UserDto userDtoFromService = userService.getUserById(userId);

        Assertions.assertEquals(userDto, userDtoFromService, "Пользователи не совпадают при поиске по id");
    }

    @Test
    @DisplayName("Должен создавать пользователя")
    void shouldCreateUser() {
        Mockito
                .when(userRepository.save(any()))
                .thenReturn(userWithId);
        UserDto userDtoFromService = userService.createUser(newUserDto);
        Assertions.assertEquals(userDto, userDtoFromService, "Пользователи не совпадают при создании");
    }

    @Test
    @DisplayName("Должен обновлять пользователя по id")
    void shouldUpdateUserById() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(userWithId));

        NewUserDto userDtoOnUpdate = NewUserDto.builder()
                .name("Timuchin")
                .build();

        UserDto updatedUserDto = UserDto.builder()
                .id(userId)
                .name("Timuchin")
                .email("drakonhftfg@yandex.ru")
                .build();

        UserDto userDtoFromService = userService.updateUser(userId, userDtoOnUpdate);
        Assertions.assertEquals(updatedUserDto, userDtoFromService, "Пользователи не совпадают при обновлении");
    }

    @Test
    @DisplayName("Должен бросать исключение при удалении несуществующего пользователя")
    void shouldThrowExceptionWhenRemoveNonExistUser() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenThrow(new NotFoundException());

        NotFoundException exceptionFromService = Assertions.assertThrows(NotFoundException.class, () -> {
            userService.removeUser(userId);
        });

        NotFoundException exception = new NotFoundException();

        Assertions.assertEquals(
                exception.getClass(),
                exceptionFromService.getClass(),
                "Не пробросилось исключение при удалении пользователя"
        );
    }

}
