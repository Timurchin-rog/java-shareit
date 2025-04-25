package ru.practicum.shareit.user.dao;

import lombok.Getter;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Getter
public class UserServiceInMemory implements UserService {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<UserDto> findAll() {
        return users.values().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(long id) {
        if (users.get(id) == null) {
            throw new NotFoundException();
        }
        User user = users.get(id);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto create(UserDto userFromRequest) {
        if (userFromRequest.getEmail() == null) {
            throw new ValidationException();
        }
        if (checkDuplicatedEmail(userFromRequest)) {
            throw new DuplicatedEmailException();
        }
        User newUser = User.builder()
                .id(getNextId())
                .email(userFromRequest.getEmail())
                .name(userFromRequest.getName())
                .build();
        users.put(newUser.getId(), newUser);
        return UserMapper.mapToUserDto(newUser);
    }

    @Override
    public UserDto update(long id, UserDto userFromRequest) {
        if (users.get(id) == null) {
            throw new NotFoundException();
        }
        if (checkDuplicatedEmail(userFromRequest)) {
            throw new DuplicatedEmailException();
        }
        User oldUser = users.get(id);
        User newUser = UserMapper.updateUserFields(oldUser, userFromRequest);
        users.put(newUser.getId(), newUser);
        return UserMapper.mapToUserDto(newUser);
    }

    @Override
    public void remove(long id) {
        users.remove(id);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private boolean checkDuplicatedEmail(UserDto userDto) {
        return users.values().stream()
                .filter(userEach -> !Objects.equals(userEach.getId(), userDto.getId()))
                .anyMatch(userEach -> userEach.getEmail().equals(userDto.getEmail()));
    }
}
