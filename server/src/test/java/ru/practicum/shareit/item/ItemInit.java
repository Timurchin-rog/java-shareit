package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.model.User;

public class ItemInit {

    protected long itemId = 1;
    protected long userId = 1;
    protected String headerCurrentUser = "X-Sharer-User-Id";
    protected String itemName = "Дрель";
    protected String itemDescription = "Работает от сети, 1300 Вт";

    protected final User user = new User(
            userId,
            "drakonhftfg@yandex.ru",
            "Timur"
    );

    protected final NewUserDto newUserDto = NewUserDto.builder()
            .name("Timur")
            .email("drakonhftfg@yandex.ru")
            .build();

    protected final NewItemDto newItemDto = NewItemDto.builder()
            .name(itemName)
            .description(itemDescription)
            .available(true)
            .build();

    protected final Item itemWithId = new Item(
            itemId,
            itemName,
            itemDescription,
            user,
            true
    );

    protected final ItemDto itemDto = ItemDto.builder()
            .id(itemId)
            .name(itemName)
            .description(itemDescription)
            .owner(user)
            .available(true)
            .build();


}
