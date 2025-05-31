package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicatedEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getUsers() {
        List<User> users = repository.findAll();
        return UserMapper.mapToUserDto(users);
    }

    @Override
    public UserDto getUserById(long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException());
        return UserMapper.mapToUserDto(user);
    }

    @Transactional
    @Override
    public UserDto createUser(NewUserDto userFromRequest) {
        checkDuplicatedEmail(userFromRequest.getEmail());
        User newUser = repository.save(UserMapper.mapFromRequest(userFromRequest));

        return UserMapper.mapToUserDto(newUser);
    }

    @Transactional
    @Override
    public UserDto updateUser(long userId, NewUserDto userFromRequest) {
        User oldUser = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException());
        checkDuplicatedEmail(userFromRequest.getEmail());
        User newUser = UserMapper.updateUserFields(oldUser, userFromRequest);
        repository.save(newUser);
        return UserMapper.mapToUserDto(newUser);
    }

    @Transactional
    @Override
    public void removeUser(long userId) {
        if (repository.findById(userId).isEmpty()) {
            throw new NotFoundException();
        }
        repository.deleteById(userId);
    }

    private void checkDuplicatedEmail(String email) {
        if (repository.findByEmailLike(email).isPresent())
            throw new DuplicatedEmailException();
    }

}
