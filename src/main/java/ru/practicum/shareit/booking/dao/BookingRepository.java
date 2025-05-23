package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBooker_Id(long bookerId, Sort sort);

    @Query("SELECT b FROM Booking b " +
            "WHERE item IN (SELECT i FROM Item i " +
            "WHERE owner = (SELECT u FROM User u " +
            "WHERE id = ?1))")
    List<Booking> findAllForItems(long ownerId, String state);

    List<Booking> findAllByBooker_IdAndItem_Id(long bookerId, long itemId);
}
