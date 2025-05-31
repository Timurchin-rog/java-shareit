package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
	private final UserClient userClient;

	@GetMapping
	public ResponseEntity<Object> getUsers() {
		return userClient.getUsers();
	}

	@GetMapping("/{user-id}")
	public ResponseEntity<Object> getUserById(@PathVariable(name = "user-id") long userId) {
		return userClient.getUserById(userId);
	}

	@PostMapping
	public ResponseEntity<Object> createUser(@Valid @RequestBody NewUserDto user) {
		return userClient.createUser(user);
	}

	@PatchMapping("/{user-id}")
	public ResponseEntity<Object> updateUser(@PathVariable(name = "user-id") long userId,
											 @RequestBody NewUserDto user) {
		return userClient.updateUser(userId, user);
	}

	@DeleteMapping("/{user-id}")
	public ResponseEntity<Object> removeUser(@PathVariable(name = "user-id") long userId) {
		return userClient.removeUser(userId);
	}
}
