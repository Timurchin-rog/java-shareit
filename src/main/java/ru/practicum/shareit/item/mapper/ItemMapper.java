package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {
    public static ItemDto mapToItemView(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owner(item.getOwner())
                .available(item.getAvailable())
                .build();
    }

    public static ItemDtoWithBooking mapToItemsView(Item item, List<Comment> comments) {
        return ItemDtoWithBooking.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owner(item.getOwner())
                .available(item.getAvailable())
                .comments(comments)
                .build();
    }

    public static List<ItemDto> mapToItemView(Iterable<Item> items) {
        List<ItemDto> itemsResult = new ArrayList<>();

        for (Item item : items) {
            itemsResult.add(mapToItemView(item));
        }

        return itemsResult;
    }

    public static Item mapToNewItem(ItemRequest item, User owner) {
        return new Item(
                item.getName(),
                item.getDescription(),
                owner,
                item.getAvailable()
        );
    }

    public static Item updateItemFields(Item item, ItemRequest itemFromRequest) {
        if (itemFromRequest.hasName()) {
            item.setName(itemFromRequest.getName());
        }
        if (itemFromRequest.hasDescription()) {
            item.setDescription(itemFromRequest.getDescription());
        }
        if (itemFromRequest.hasOwner()) {
            item.setDescription(itemFromRequest.getDescription());
        }
        if (itemFromRequest.hasAvailable()) {
            item.setAvailable(itemFromRequest.getAvailable());
        }
        return item;
    }

}
