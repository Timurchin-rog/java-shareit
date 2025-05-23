package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicatedEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> findAll() {
        List<User> users = repository.findAll();
        return UserMapper.mapToUserDto(users);
    }

    @Override
    public UserDto findById(long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        return UserMapper.mapToUserDto(user);
    }

    @Transactional
    @Override
    public UserDto create(UserDto userFromRequest) {
        checkValidation(userFromRequest);
        if (checkDuplicatedEmail(userFromRequest)) {
            throw new DuplicatedEmailException();
        }
        User newUser = repository.save(UserMapper.mapToNewUser(userFromRequest));

        return UserMapper.mapToUserDto(newUser);
    }

    @Transactional
    @Override
    public UserDto update(long id, UserDto userFromRequest) {
        User oldUser = repository.findById(id)
                .orElseThrow(() -> new NotFoundException());

        if (checkDuplicatedEmail(userFromRequest)) {
            throw new DuplicatedEmailException();
        }
        User newUser = UserMapper.updateUserFields(oldUser, userFromRequest);
        repository.save(newUser);
        return UserMapper.mapToUserDto(newUser);
    }

    @Transactional
    @Override
    public void remove(long id) {
        if (repository.findById(id).isEmpty()) {
            throw new NotFoundException();
        }
        repository.deleteById(id);
    }

    private void checkValidation(UserDto user) {
        if (user.getEmail() == null || user.getEmail().isBlank())
            throw new ValidationException();
        if (!user.getEmail().contains("@"))
            throw new ValidationException();
        if (user.getName() == null || user.getName().isBlank())
            throw new ValidationException();
    }

    private boolean checkDuplicatedEmail(UserDto userDto) {
        return repository.findAll().stream()
                .filter(userEach -> !Objects.equals(userEach.getId(), userDto.getId()))
                .anyMatch(userEach -> userEach.getEmail().equals(userDto.getEmail()));
    }

}
