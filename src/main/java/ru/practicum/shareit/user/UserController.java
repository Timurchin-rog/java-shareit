package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable long id) {
        return userService.findById(id);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) {
        return userService.create(user);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable long id, @Valid @RequestBody UserDto user) {
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable long id) {
        userService.remove(id);
    }
}