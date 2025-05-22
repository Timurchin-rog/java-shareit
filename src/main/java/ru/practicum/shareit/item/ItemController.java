package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dao.ItemService;
import ru.practicum.shareit.item.dto.CommentView;
import ru.practicum.shareit.item.dto.ItemRequest;
import ru.practicum.shareit.item.dto.ItemView;
import ru.practicum.shareit.item.dto.ItemsView;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final String headerOwner = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemView> findAll(@RequestHeader(value = headerOwner) long ownerId) {
        return itemService.findAll(ownerId);
    }

    @GetMapping("/{id}")
    public ItemsView findById(@PathVariable long id) {
        return itemService.findById(id);
    }

    @GetMapping("/search")
    public List<ItemView> findByText(@RequestParam String text) {
        return itemService.findByText(text);
    }

    @PostMapping
    public ItemView create(@RequestHeader(value = headerOwner) long ownerId,
                           @RequestBody ItemRequest item) {
        return itemService.create(ownerId, item);
    }

    @PostMapping("/{id}/comment")
    public CommentView createComment(@PathVariable long id,
                                     @RequestHeader(value = headerOwner) long authorId,
                                     @RequestBody Comment comment) {
        return itemService.createComment(id, authorId, comment);
    }

    @PatchMapping("/{id}")
    public ItemView update(@PathVariable long id,
                           @RequestHeader(value = headerOwner) long ownerId,
                           @RequestBody ItemRequest item) {
        return itemService.update(id, ownerId, item);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable long id, @RequestHeader(value = "X-Sharer-User-Id") long ownerId) {
        itemService.remove(id, ownerId);
    }
}
