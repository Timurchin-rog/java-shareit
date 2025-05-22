package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.CommentView;
import ru.practicum.shareit.item.dto.ItemRequest;
import ru.practicum.shareit.item.dto.ItemView;
import ru.practicum.shareit.item.dto.ItemsView;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ItemService {
    List<ItemView> findAll(long ownerId);

    ItemsView findById(long id);

    List<ItemView> findByText(String text);

    ItemView create(long ownerId, ItemRequest item);

    CommentView createComment(long id, long ownerId, Comment comment);

    ItemView update(long id, long ownerId, ItemRequest itemRequest);

    void remove(long id, long ownerId);
}
