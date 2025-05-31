package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static List<UserDto> mapToUserDto(Iterable<User> users) {
        List<UserDto> usersResult = new ArrayList<>();

        for (User user : users) {
            usersResult.add(mapToUserDto(user));
        }

        return usersResult;
    }

    public static User mapFromRequest(NewUserDto user) {
        return new User(
                user.getEmail(),
                user.getName()
        );
    }

    public static User updateUserFields(User user, NewUserDto userFromRequest) {
        if (userFromRequest.hasEmail()) {
            if (!user.getEmail().contains("@"))
                throw new ValidationException();
            user.setEmail(userFromRequest.getEmail());
        }
        if (userFromRequest.hasName()) {
            user.setName(userFromRequest.getName());
        }
        return user;
    }

}
