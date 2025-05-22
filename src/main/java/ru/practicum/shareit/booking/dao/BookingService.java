package ru.practicum.shareit.booking.dao;

import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingView;

import java.util.List;

public interface BookingService {
    List<BookingView> findAllOutgoing(long bookerId, String state);

    List<BookingView> findAllIncoming(long ownerId, String state);

    BookingView findById(long id, long userId);

    BookingView create(long bookerId, BookingRequest booking);

    BookingView updateStatus(long id, long ownerId, boolean approved);
}
