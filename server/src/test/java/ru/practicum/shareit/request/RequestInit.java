package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestLongDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class RequestInit {
    protected long requestId = 1;
    protected long itemId = 1;
    protected long userId = 1;
    protected String headerCurrentUser = "X-Sharer-User-Id";
    protected String requestDescription = "Нужна дрель";
    protected String itemName = "Дрель";
    protected String itemDescription = "Работает от сети, 1300 Вт";
    protected String userEmail = "drakonhftfg@yandex.ru";
    protected String userName = "Timur";

    protected final User userWithId = new User(
            userId,
            userEmail,
            userName
    );

    protected final ItemShortDto itemShort = new ItemShortDto(
            itemId,
            itemName,
            userId
    );

    protected final NewRequestDto newRequestDto = NewRequestDto.builder()
            .description(requestDescription)
            .build();

    protected final Request requestWithId = new Request(
            requestId,
            requestDescription,
            userWithId
    );

    protected final RequestDto requestDto = RequestDto.builder()
            .id(requestId)
            .description(requestDescription)
            .requestor(userWithId)
            .build();

    protected final RequestLongDto requestLong = RequestLongDto.builder()
            .id(requestId)
            .description(requestDescription)
            .requestor(userWithId)
            .items(List.of(itemShort))
            .build();

    protected final NewUserDto newUserDto = NewUserDto.builder()
            .name(userName)
            .email(userEmail)
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
            userWithId,
            true
    );
}
