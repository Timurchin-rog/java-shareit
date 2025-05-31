package ru.practicum.shareit.booking.dao;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.util.List;

public interface BookingService {
    List<BookingDto> getBookingsOutgoing(long bookerId, String state);

    List<BookingDto> getBookingsIncoming(long ownerId, String state);

    BookingDto getBookingById(long bookingId, long userId);

    BookingDto createBooking(long bookerId, NewBookingDto booking);

    BookingDto updateStatusOfBooking(long bookingId, long ownerId, boolean approved);
}
