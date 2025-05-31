package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserInit {

    protected long userId = 1;
    protected String userEmail = "drakonhftfg@yandex.ru";
    protected String userName = "Timur";

    protected final NewUserDto newUserDto = NewUserDto.builder()
            .email(userEmail)
            .name(userName)
            .build();

    protected final User userWithId = new User(
            userId,
            userEmail,
            userName
    );

    protected final UserDto userDto = UserDto.builder()
            .id(userId)
            .email(userEmail)
            .name(userName)
            .build();
}
