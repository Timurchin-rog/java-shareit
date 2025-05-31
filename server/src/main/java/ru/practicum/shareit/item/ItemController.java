package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dao.ItemService;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final String headerCurrentUser = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader(value = headerCurrentUser) long ownerId) {
        return itemService.getItems(ownerId);
    }

    @GetMapping("/{item-id}")
    public ItemLongDto getItemById(@PathVariable(name = "item-id") long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByText(@RequestParam String text) {
        return itemService.getItemsByText(text);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(value = headerCurrentUser) long ownerId,
                              @RequestBody NewItemDto item) {
        return itemService.createItem(ownerId, item);
    }

    @PostMapping("/{item-id}/comment")
    public CommentDto createComment(@PathVariable(name = "item-id") long itemId,
                                    @RequestHeader(value = headerCurrentUser) long authorId,
                                    @RequestBody NewCommentDto comment) {
        return itemService.createComment(itemId, authorId, comment);
    }

    @PatchMapping("/{item-id}")
    public ItemDto updateItem(@PathVariable(name = "item-id") long itemId,
                              @RequestHeader(value = headerCurrentUser) long ownerId,
                              @RequestBody NewItemDto item) {
        return itemService.updateItem(itemId, ownerId, item);
    }

    @DeleteMapping("/{item-id}")
    public void removeItem(@PathVariable(name = "item-id") long itemId,
                           @RequestHeader(value = headerCurrentUser) long ownerId) {
        itemService.removeItem(itemId, ownerId);
    }
}
