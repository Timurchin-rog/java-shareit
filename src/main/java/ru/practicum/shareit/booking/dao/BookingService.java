package ru.practicum.shareit.booking.dao;

import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    List<BookingDto> findAllOutgoing(long bookerId, String state);

    List<BookingDto> findAllIncoming(long ownerId, String state);

    BookingDto findById(long id, long userId);

    BookingDto create(long bookerId, BookingRequest booking);

    BookingDto updateStatus(long id, long ownerId, boolean approved);
}
