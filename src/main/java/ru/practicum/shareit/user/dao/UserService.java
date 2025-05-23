package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();

    UserDto findById(long id);

    UserDto create(UserDto userDto);

    UserDto update(long id, UserDto userDto);

    void remove(long id);
}
