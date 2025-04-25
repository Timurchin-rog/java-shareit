package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> findAll(long ownerId);

    ItemDto findById(long id);

    Collection<ItemDto> findByText(String text);

    ItemDto create(long ownerId, ItemDto itemDto);

    ItemDto update(long id, long ownerId, ItemDto itemDto);

    void remove(long id, long ownerId);
}
