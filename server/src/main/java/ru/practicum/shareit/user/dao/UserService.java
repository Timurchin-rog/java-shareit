package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    UserDto getUserById(long userId);

    UserDto createUser(NewUserDto user);

    UserDto updateUser(long userId, NewUserDto user);

    void removeUser(long userId);
}
