package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@RequiredArgsConstructor
public class ItemMapper {
    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwnerId())
                .available(item.getAvailable())
                .reviews(item.getReviews())
                .build();
    }

    public static Item updateItemFields(Item item, ItemDto itemFromRequest) {
        if (itemFromRequest.hasName()) {
            item.setName(itemFromRequest.getName());
        }
        if (itemFromRequest.hasDescription()) {
            item.setDescription(itemFromRequest.getDescription());
        }
        if (itemFromRequest.hasAvailable()) {
            item.setAvailable(itemFromRequest.getAvailable());
        }
        return item;
    }
}
