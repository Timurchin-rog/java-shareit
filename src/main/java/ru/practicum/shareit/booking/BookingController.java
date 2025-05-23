package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dao.BookingService;
import ru.practicum.shareit.booking.dto.BookingRequest;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final String headerUser = "X-Sharer-User-Id";

    @GetMapping
    public List<BookingDto> findAllOutgoing(@RequestHeader(value = headerUser) long bookerId,
                                            @RequestParam(defaultValue = "all") String state) {
        return bookingService.findAllOutgoing(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllIncoming(@RequestHeader(value = headerUser) long ownerId,
                                            @RequestParam(defaultValue = "all") String state) {
        return bookingService.findAllIncoming(ownerId, state);
    }

    @GetMapping("/{id}")
    public BookingDto findById(@PathVariable long id,
                               @RequestHeader(value = headerUser) long userId) {
        return bookingService.findById(id, userId);
    }

    @PostMapping
    public BookingDto create(@RequestHeader(value = headerUser) long bookerId,
                             @RequestBody BookingRequest booking) {
        return bookingService.create(bookerId, booking);
    }

    @PatchMapping("/{id}")
    public BookingDto updateStatus(@PathVariable long id,
                                   @RequestHeader(value = headerUser) long ownerId,
                                   @RequestParam boolean approved) {
        return bookingService.updateStatus(id, ownerId, approved);
    }

}
