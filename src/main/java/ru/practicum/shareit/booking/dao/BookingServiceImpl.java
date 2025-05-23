package ru.practicum.shareit.booking.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<BookingDto> findAllOutgoing(long bookerId, String state) {
        List<Booking> bookings;
        if (state.toLowerCase().equals("current"))
            bookings = bookingRepository
                    .findAllByBooker_IdAndStartIsBeforeAndEndIsAfter(
                            bookerId,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    );
        else if (state.toLowerCase().equals("past")) {
            bookings = bookingRepository
                    .findAllByBooker_IdAndEndIsBefore(
                            bookerId,
                            LocalDateTime.now()
                    );
        }
        else if (state.toLowerCase().equals("future")) {
            bookings = bookingRepository
                    .findAllByBooker_IdAndStartIsAfter(
                            bookerId,
                            LocalDateTime.now()
                    );
        }
        else if (state.toLowerCase().equals("waiting")
        | state.toLowerCase().equals("rejected")) {
            bookings = bookingRepository.findAllByBooker_IdAndStatus(bookerId, state);
        }
        else
            bookings = bookingRepository.findAllByBooker_Id(bookerId);
        return BookingMapper.mapToBookingDto(bookings);
    }

    @Override
    public List<BookingDto> findAllIncoming(long ownerId, String state) {
        if (userRepository.findById(ownerId).isEmpty())
                throw new NotFoundException();
        List<Item> itemsOfOwner = itemRepository.findAllByOwner_id(ownerId);
        if (itemsOfOwner.isEmpty())
            throw new NotItemsException();
        List<Booking> bookings;
        if (state.toLowerCase().equals("current"))
            bookings = bookingRepository
                    .findAllByItemInAndStartIsBeforeAndEndIsAfter(
                            itemsOfOwner,
                            LocalDateTime.now(),
                            LocalDateTime.now()
                    );
        else if (state.toLowerCase().equals("past")) {
            bookings = bookingRepository
                    .findAllByItemInAndEndIsBefore(
                            itemsOfOwner,
                            LocalDateTime.now()
                    );
        }
        else if (state.toLowerCase().equals("future")) {
            bookings = bookingRepository
                    .findAllByItemInAndStartIsAfter(
                            itemsOfOwner,
                            LocalDateTime.now()
                    );
        }
        else if (state.toLowerCase().equals("waiting")
                | state.toLowerCase().equals("rejected")) {
            bookings = bookingRepository.findAllByItemInAndStatus(itemsOfOwner, state);
        }
        else
            bookings = bookingRepository.findAllByItemIn(itemsOfOwner);
        return BookingMapper.mapToBookingDto(bookings);
    }

    @Override
    public BookingDto findById(long id, long userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        return BookingMapper.mapToBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto create(long bookerId, BookingRequest booking) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException());
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException());
        if (!item.getAvailable())
            throw new NotAvailableItemException();
        checkValidation(booking);
        Booking newBooking = bookingRepository.save(BookingMapper.mapToNewBooking(booking, item, booker));
        return BookingMapper.mapToBookingDto(newBooking);
    }

    @Transactional
    @Override
    public BookingDto updateStatus(long id, long ownerId, boolean approved) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        Item item = booking.getItem();
        if (item.getOwner().getId() != ownerId)
            throw new NotOwnerException();
        if (approved)
            booking.setStatus(Status.APPROVED);
        else
            booking.setStatus(Status.REJECTED);
        return BookingMapper.mapToBookingDto(booking);
    }

    private void checkValidation(BookingRequest booking) {
        if (booking.getStart().isAfter(booking.getEnd())
        | booking.getStart().isEqual(booking.getEnd()))
            throw new ValidationException();
    }
}