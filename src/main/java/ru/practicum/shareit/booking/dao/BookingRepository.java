package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBooker_Id(long bookerId);

    List<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfter(
            long bookerId,
            LocalDateTime now1,
            LocalDateTime now2
    );

    List<Booking> findAllByBooker_IdAndEndIsBefore(
            long bookerId,
            LocalDateTime now
    );

    List<Booking> findAllByBooker_IdAndStartIsAfter(
            long bookerId,
            LocalDateTime now
    );

    List<Booking> findAllByBooker_IdAndStatus(long bookerId, String state);

    List<Booking> findAllByItemIn(List<Item> itemsOfOwner);

    List<Booking> findAllByItemInAndStartIsBeforeAndEndIsAfter(
            List<Item> itemsOfOwner,
            LocalDateTime now1,
            LocalDateTime now2
    );

    List<Booking> findAllByItemInAndEndIsBefore(
            List<Item> itemsOfOwner,
            LocalDateTime now
    );

    List<Booking> findAllByItemInAndStartIsAfter(
            List<Item> itemsOfOwner,
            LocalDateTime now
    );

    List<Booking> findAllByItemInAndStatus(
            List<Item> itemsOfOwner,
            String state
    );

    List<Booking> findAllByBooker_IdAndItem_Id(long bookerId, long itemId);
}
