package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItems(long ownerId);

    ItemLongDto getItemById(long itemId);

    List<ItemDto> getItemsByText(String text);

    ItemDto createItem(long ownerId, NewItemDto item);

    CommentDto createComment(long itemId, long ownerId, NewCommentDto comment);

    ItemDto updateItem(long itemId, long ownerId, NewItemDto itemRequest);

    void removeItem(long itemId, long ownerId);
}
