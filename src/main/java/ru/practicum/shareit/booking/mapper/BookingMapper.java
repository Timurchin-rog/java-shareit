package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {
    public static BookingView mapToBookingView(Booking booking) {
        return BookingView.builder()
                .id(booking.getId())
                .item(booking.getItem())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    public static List<BookingView> mapToBookingView(Iterable<Booking> bookings) {
        List<BookingView> bookingsResult = new ArrayList<>();

        for (Booking booking : bookings) {
            bookingsResult.add(mapToBookingView(booking));
        }

        return bookingsResult;
    }

    public static Booking mapToNewBooking(BookingRequest booking, Item item, User booker) {
        return new Booking(
                item,
                booking.getStart(),
                booking.getEnd(),
                booker,
                Status.WAITING
        );
    }

}
