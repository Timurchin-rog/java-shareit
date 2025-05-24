package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ItemService {
    List<ItemDto> findAll(long ownerId);

    ItemDtoFull findById(long id);

    List<ItemDto> findByText(String text);

    ItemDto create(long ownerId, ItemRequest item);

    CommentDto createComment(long id, long ownerId, Comment comment);

    ItemDto update(long id, long ownerId, ItemRequest itemRequest);

    void remove(long id, long ownerId);
}
