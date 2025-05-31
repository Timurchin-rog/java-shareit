package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dao.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookingService")
public class BookingServiceTest extends BookingInit {

    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookingRepository bookingRepository;

    BookingServiceImpl bookingService;

    @BeforeEach
    void init() {
        bookingService = new BookingServiceImpl(
                bookingRepository,
                userRepository,
                itemRepository
        );
    }

    @Test
    @DisplayName("Должен получить бронирования чужих вещей текущим пользователем")
    void shouldGetOutgoingBookings() {
        List<Booking> bookings = List.of(bookingWithId);
        Mockito
                .when(bookingRepository.findAllByBooker_IdAndStatus(anyLong(), anyString()))
                .thenReturn(bookings);

        List<BookingDto> bookingsDto = BookingMapper.mapToBookingDto(bookings);
        List<BookingDto> bookingDtoFromService = bookingService.getBookingsOutgoing(userId, "waiting");

        Assertions.assertEquals(
                bookingsDto,
                bookingDtoFromService,
                "Списки исходящих бронирований не совпадают при получении");
    }

//    @Test
//    @DisplayName("Должен получить бронирования вещей текущего пользователя")
//    void shouldGetIncomingBookings() {
//        List<Item> items = List.of(itemWithId);
//        Mockito.
//                when(itemRepository.findAllByOwner_id(anyLong()))
//                .thenReturn(items);
//        Booking bookingApproved = bookingWithId;
//        bookingApproved.setStatus(BookingState.APPROVED);
//        List<Booking> bookings = List.of(bookingApproved);
//        Mockito.
//                when(bookingRepository.findAllByItem_IdIn(
//                        anyList()
//                ))
//                .thenReturn(bookings);
//
//        List<BookingDto> bookingsDto = BookingMapper.mapToBookingDto(bookings);
//        List<BookingDto> bookingDtoFromService = bookingService.getBookingsOutgoing(userId, "all");
//
//        Assertions.assertEquals(
//                bookingsDto,
//                bookingDtoFromService,
//                "Списки входящих бронирований не совпадают при получении");
//    }

    @Test
    @DisplayName("Должен находить бронирование по id")
    void shouldFindItemsByText() {
        Mockito
                .when(bookingRepository.findById(bookingId))
                .thenReturn(Optional.of(bookingWithId));

        BookingDto bookingDtoFromService = bookingService.getBookingById(bookingId, userId);

        Assertions.assertEquals(
                bookingDto,
                bookingDtoFromService,
                "Бронирования не совпадают при поиске по id"
        );
    }

    @Test
    @DisplayName("Должен создавать бронирование")
    void shouldCreateBooking() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(userWithId));

        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemWithId));

        Mockito
                .when(bookingRepository.save(any()))
                .thenReturn(bookingWithId);

        BookingDto bookingFromService = bookingService.createBooking(userId, newBookingDto);
        Assertions.assertEquals(bookingDto, bookingFromService, "Бронирования не совпадают при создании");
    }

    @Test
    @DisplayName("Должен обновлять статус бронирования по id")
    void shouldUpdateStatusOfBooking() {
        Mockito
                .when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.of(bookingWithId));

        BookingDto bookingFromService = bookingService.updateStatusOfBooking(bookingId, userId, true);
        BookingDto bookingApprovedDto = bookingDto.toBuilder().status(BookingState.APPROVED).build();

        Assertions.assertEquals(bookingApprovedDto, bookingFromService, "Бронирования не совпадают при обновлении");
    }
}
