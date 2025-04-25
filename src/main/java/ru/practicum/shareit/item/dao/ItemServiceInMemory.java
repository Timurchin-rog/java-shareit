package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserServiceInMemory;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceInMemory implements ItemService {
    private final UserServiceInMemory userService;
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public List<ItemDto> findAll(long ownerId) {
        return items.values().stream()
                .filter(item -> Objects.equals(userService.getUsers().get(ownerId).getId(), item.getOwnerId()))
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findById(long id) {
        if (items.get(id) == null) {
            throw new NotFoundException();
        }
        Item item = items.get(id);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> findByText(String text) {
        if (text == null || text.isBlank())
            return new ArrayList<>();
        else
            return items.values().stream()
                    .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                            | item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .filter(Item::getAvailable)
                    .map(ItemMapper::mapToItemDto)
                    .collect(Collectors.toList());
    }

    @Override
    public ItemDto create(long ownerId, ItemDto itemFromRequest) {
        if (userService.getUsers().get(ownerId) == null) {
            throw new NotFoundException();
        }
        checkValidation(itemFromRequest);
        Item newItem = Item.builder()
                .id(getNextId())
                .name(itemFromRequest.getName())
                .description(itemFromRequest.getDescription())
                .ownerId(ownerId)
                .available(itemFromRequest.getAvailable())
                .build();
        items.put(newItem.getId(), newItem);
        return ItemMapper.mapToItemDto(newItem);
    }

    @Override
    public ItemDto update(long id, long ownerId, ItemDto itemFromRequest) {
        if (items.get(id) == null) {
            throw new NotFoundException();
        }
        Item oldItem = items.get(id);
        if (oldItem.getOwnerId() != ownerId) {
            throw new NotOwnerException();
        }
        Item newItem = ItemMapper.updateItemFields(oldItem, itemFromRequest);
        items.put(newItem.getId(), newItem);
        return ItemMapper.mapToItemDto(newItem);
    }

    @Override
    public void remove(long id, long ownerId) {
        Item item = items.get(id);
        if (item.getOwnerId() != ownerId) {
            throw new NotOwnerException();
        }
        items.remove(id);
    }

    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void checkValidation(ItemDto item) {
        if (item.getName() == null || item.getName().isBlank())
            throw new ValidationException();
        if (item.getDescription() == null || item.getDescription().isBlank())
            throw new ValidationException();
        if (item.getAvailable() == null)
            throw new ValidationException();
    }
}
