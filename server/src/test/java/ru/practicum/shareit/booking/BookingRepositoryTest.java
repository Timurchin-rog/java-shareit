package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Transactional
@SpringBootTest(
        properties = "jdbc:h2:mem:shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("BookingRepository")
public class BookingRepositoryTest extends BookingInit {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Test
    @DisplayName("Должен находить бронирования по id бронировавшего")
    void shouldFindBookingsByBrokerId() {
        User userFromRep = userRepository.save(UserMapper.mapFromRequest(newUserDto));
        Item itemFromRep = itemRepository.save(ItemMapper.mapFromRequest(newItemDto, userFromRep));
        Booking bookingFromRep = bookingRepository.save(
                BookingMapper.mapFromRequest(newBookingDto, itemFromRep, userFromRep)
        );
        List<Booking> bookingFromRepository = bookingRepository.findAllByBooker_Id(userFromRep.getId());
        Assertions.assertEquals(
                List.of(bookingFromRep),
                bookingFromRepository,
                "Списки бронирований не совпадают при поиске по id бронировавшего в БД"
        );
    }
}
