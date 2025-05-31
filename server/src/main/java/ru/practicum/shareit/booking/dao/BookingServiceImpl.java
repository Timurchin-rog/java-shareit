package ru.practicum.shareit.booking.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotItemsException;
import ru.practicum.shareit.exception.NotOwnerException;
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
    public List<BookingDto> getBookingsOutgoing(long bookerId, String state) {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;
        if (state.equalsIgnoreCase("current")) {
            bookings = bookingRepository
                    .findAllByBooker_IdAndStartIsBeforeAndEndIsAfter(
                            bookerId,
                            now,
                            now
                    );
        } else if (state.equalsIgnoreCase("past")) {
            bookings = bookingRepository
                    .findAllByBooker_IdAndEndIsBefore(
                            bookerId,
                            now
                    );
        } else if (state.equalsIgnoreCase("future")) {
            bookings = bookingRepository
                    .findAllByBooker_IdAndStartIsAfter(
                            bookerId,
                            now
                    );
        } else if (state.equalsIgnoreCase("waiting")
                | state.equalsIgnoreCase("rejected")) {
            bookings = bookingRepository.findAllByBooker_IdAndStatus(bookerId, state);
        } else
            bookings = bookingRepository.findAllByBooker_Id(bookerId);
        return BookingMapper.mapToBookingDto(bookings);
    }

    @Override
    public List<BookingDto> getBookingsIncoming(long ownerId, String state) {
        LocalDateTime now = LocalDateTime.now();
        if (userRepository.findById(ownerId).isEmpty())
                throw new NotFoundException();
        List<Item> itemsOfOwner = itemRepository.findAllByOwner_id(ownerId);
        if (itemsOfOwner.isEmpty())
            throw new NotItemsException();
        List<Booking> bookings;
        if (state.equalsIgnoreCase("current")) {
            bookings = bookingRepository
                    .findAllByItemInAndStartIsBeforeAndEndIsAfter(
                            itemsOfOwner,
                            now,
                            now
                    );
        } else if (state.equalsIgnoreCase("past")) {
            bookings = bookingRepository
                    .findAllByItemInAndEndIsBefore(
                            itemsOfOwner,
                            now
                    );
        } else if (state.equalsIgnoreCase("future")) {
            bookings = bookingRepository
                    .findAllByItemInAndStartIsAfter(
                            itemsOfOwner,
                            now
                    );
        } else if (state.equalsIgnoreCase("waiting")
                | state.equalsIgnoreCase("rejected")) {
            bookings = bookingRepository.findAllByItemInAndStatus(itemsOfOwner, state);
        } else
            bookings = bookingRepository.findAllByItemIn(itemsOfOwner);
        return BookingMapper.mapToBookingDto(bookings);
    }

    @Override
    public BookingDto getBookingById(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException());
        return BookingMapper.mapToBookingDto(booking);
    }

    @Transactional
    @Override
    public BookingDto createBooking(long bookerId, NewBookingDto booking) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException());
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new NotFoundException());
        if (!item.getAvailable())
            throw new NotAvailableItemException();
        Booking newBooking = bookingRepository.save(BookingMapper.mapFromRequest(booking, item, booker));
        return BookingMapper.mapToBookingDto(newBooking);
    }

    @Transactional
    @Override
    public BookingDto updateStatusOfBooking(long bookingId, long ownerId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException());
        Item item = booking.getItem();
        if (item.getOwner().getId() != ownerId)
            throw new NotOwnerException();
        if (approved)
            booking.setStatus(BookingState.APPROVED);
        else
            booking.setStatus(BookingState.REJECTED);
        return BookingMapper.mapToBookingDto(booking);
    }

}