package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dao.UserService;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{user-id}")
    public UserDto getUserById(@PathVariable(name = "user-id") long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDto createUser(@RequestBody NewUserDto user) {
        return userService.createUser(user);
    }

    @PatchMapping("/{user-id}")
    public UserDto updateUser(@PathVariable(name = "user-id") long userId,
                              @RequestBody NewUserDto user) {
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{user-id}")
    public void removeUser(@PathVariable(name = "user-id") long userId) {
        userService.removeUser(userId);
    }
}