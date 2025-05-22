package ru.practicum.shareit.booking.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingView;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotItemsException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<BookingView> findAllOutgoing(long bookerId, String state) {
        List<Booking> bookings = bookingRepository.findAllByBooker_Id(bookerId, Sort.by("end"));
        return BookingMapper.mapToBookingView(bookings);
    }

    @Override
    public List<BookingView> findAllIncoming(long ownerId, String state) {
        if (userRepository.findById(ownerId).isEmpty())
                throw new NotFoundException();
        if (itemRepository.findAllByOwner_id(ownerId).isEmpty())
            throw new NotItemsException();
        List<Booking> bookings = bookingRepository.findAllForItems(ownerId, state);
        return BookingMapper.mapToBookingView(bookings);
    }

    @Override
    public BookingView findById(long id, long userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        return BookingMapper.mapToBookingView(booking);
    }

    @Transactional
    @Override
    public BookingView create(long bookerId, BookingRequest booking) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException());
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException());
        if (!item.getAvailable())
            throw new NotAvailableItemException();
        Booking newBooking = bookingRepository.save(BookingMapper.mapToNewBooking(booking, item, booker));
        return BookingMapper.mapToBookingView(newBooking);
    }

    @Transactional
    @Override
    public BookingView updateStatus(long id, long ownerId, boolean approved) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        Item item = booking.getItem();
        if (item.getOwner().getId() != ownerId)
            throw new NotOwnerException();
        if (approved)
            booking.setStatus(Status.APPROVED);
        else
            booking.setStatus(Status.REJECTED);
        return BookingMapper.mapToBookingView(booking);
    }
}