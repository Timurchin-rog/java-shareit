package ru.practicum.shareit.user.mapper;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@RequiredArgsConstructor
public final class UserMapper {
    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static User updateUserFields(User user, UserDto userFromRequest) {
        if (userFromRequest.hasEmail()) {
            user.setEmail(userFromRequest.getEmail());
        }
        if (userFromRequest.hasName()) {
            user.setName(userFromRequest.getName());
        }
        return user;
    }
}
