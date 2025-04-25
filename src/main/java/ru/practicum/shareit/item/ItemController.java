package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public Collection<ItemDto> findAll(@RequestHeader(value = "X-Sharer-User-Id") long ownerId) {
        return itemService.findAll(ownerId);
    }

    @GetMapping("/{id}")
    public ItemDto findById(@PathVariable long id) {
        return itemService.findById(id);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findByText(@RequestParam String text) {
        return itemService.findByText(text);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(value = "X-Sharer-User-Id") long ownerId,
                          @Valid @RequestBody ItemDto item) {
        return itemService.create(ownerId, item);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable long id,
                          @RequestHeader(value = "X-Sharer-User-Id") long ownerId,
                          @Valid @RequestBody ItemDto item) {
        return itemService.update(id, ownerId, item);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable long id, @RequestHeader(value = "X-Sharer-User-Id") long ownerId) {
        itemService.remove(id, ownerId);
    }
}
