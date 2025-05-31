package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class BookingInit {
    protected long bookingId = 1;
    protected long itemId = 1;
    protected long userId = 1;
    protected String headerCurrentUser = "X-Sharer-User-Id";
    protected String itemName = "Дрель";
    protected String itemDescription = "Работает от сети, 1300 Вт";
    protected String userEmail = "drakonhftfg@yandex.ru";
    protected String userName = "Timur";

    protected final NewBookingDto newBookingDto = NewBookingDto.builder()
            .itemId(itemId)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .build();

    protected final NewUserDto newUserDto = NewUserDto.builder()
            .name(userName)
            .email(userEmail)
            .build();

    protected final User userWithId = new User(
            userId,
            userEmail,
            userName
    );

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

    protected final Booking bookingWithId = new Booking(
            bookingId,
            itemWithId,
            userWithId,
            BookingState.WAITING
    );

    protected final BookingDto bookingDto = BookingDto.builder()
            .id(bookingId)
            .item(itemWithId)
            .booker(userWithId)
            .status(BookingState.WAITING)
            .build();
}
