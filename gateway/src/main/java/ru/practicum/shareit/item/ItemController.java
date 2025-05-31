package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.NewItemDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
	private final ItemClient itemClient;
	private final String headerCurrentUser = "X-Sharer-User-Id";

	@GetMapping
	public ResponseEntity<Object> getItems(@RequestHeader(value = headerCurrentUser) long ownerId) {
		return itemClient.getItems(ownerId);
	}

	@GetMapping("/{item-id}")
	public ResponseEntity<Object> getItemById(@PathVariable(name = "item-id") long itemId) {
		return itemClient.getItemById(itemId);
	}

	@GetMapping("/search")
	public ResponseEntity<Object> getItemsByText(@RequestParam String text) {
		return itemClient.getItemsByText(text);
	}

	@PostMapping
	public ResponseEntity<Object> createItem(@RequestHeader(value = headerCurrentUser) long ownerId,
											 @Valid @RequestBody NewItemDto item) {
		return itemClient.createItem(ownerId, item);
	}

	@PostMapping("/{item-id}/comment")
	public ResponseEntity<Object> createComment(@PathVariable(name = "item-id") long itemId,
												@RequestHeader(value = headerCurrentUser) long authorId,
												@Valid @RequestBody NewCommentDto comment) {
		return itemClient.createComment(itemId, authorId, comment);
	}

	@PatchMapping("/{item-id}")
	public ResponseEntity<Object> updateItem(@PathVariable(name = "item-id") long itemId,
											 @RequestHeader(value = headerCurrentUser) long ownerId,
							                 @RequestBody NewItemDto item) {
		return itemClient.updateItem(itemId, ownerId, item);
	}

	@DeleteMapping("/{item-id}")
	public ResponseEntity<Object> removeItem(@PathVariable(name = "item-id") long itemId,
											 @RequestHeader(value = headerCurrentUser) long ownerId) {
		return itemClient.removeItem(itemId, ownerId);
	}
}
