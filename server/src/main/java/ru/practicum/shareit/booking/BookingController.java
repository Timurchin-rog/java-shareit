package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dao.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final String headerCurrentUser = "X-Sharer-User-Id";

    @GetMapping
    public List<BookingDto> getBookingsOutgoing(@RequestHeader(value = headerCurrentUser) long bookerId,
                                                @RequestParam(defaultValue = "all") String state) {
        return bookingService.getBookingsOutgoing(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsIncoming(@RequestHeader(value = headerCurrentUser) long ownerId,
                                                @RequestParam(defaultValue = "all") String state) {
        return bookingService.getBookingsIncoming(ownerId, state);
    }

    @GetMapping("/{booking-id}")
    public BookingDto getBookingById(@PathVariable(name = "booking-id") long bookingId,
                                     @RequestHeader(value = headerCurrentUser) long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @PostMapping
    public BookingDto createBooking(@RequestHeader(value = headerCurrentUser) long bookerId,
                                    @RequestBody NewBookingDto booking) {
        return bookingService.createBooking(bookerId, booking);
    }

    @PatchMapping("/{booking-id}")
    public BookingDto updateStatusOfBooking(@PathVariable(name = "booking-id") long bookingId,
                                            @RequestHeader(value = headerCurrentUser) long ownerId,
                                            @RequestParam boolean approved) {
        return bookingService.updateStatusOfBooking(bookingId, ownerId, approved);
    }

}
