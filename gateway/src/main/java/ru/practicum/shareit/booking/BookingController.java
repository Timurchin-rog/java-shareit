package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.NewBookingDto;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
	private final BookingClient bookingClient;
	private final String headerCurrentUser = "X-Sharer-User-Id";

	@GetMapping
	public ResponseEntity<Object> getBookingsOutgoing(@RequestHeader(headerCurrentUser) long bookerId,
													  @RequestParam(defaultValue = "all") String state) {
		if (BookingState.from(state).isEmpty())
			throw new IllegalArgumentException("Unknown state: " + state);
		return bookingClient.getBookingsOutgoing(bookerId, state);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingsIncoming(@RequestHeader(headerCurrentUser) long ownerId,
													  @RequestParam(defaultValue = "all") String state) {
		BookingState stateOfEnum = BookingState.from(state)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
		return bookingClient.getBookingsIncoming(ownerId, stateOfEnum);
	}

	@GetMapping("/{booking-id}")
	public ResponseEntity<Object> getBookingById(@PathVariable(name = "booking-id") long bookingId,
												 @RequestHeader(value = headerCurrentUser) long userId) {
		return bookingClient.getBookingById(bookingId, userId);
	}

	@PostMapping
	public ResponseEntity<Object> createBooking(@RequestHeader(value = headerCurrentUser) long bookerId,
												@Valid @RequestBody NewBookingDto booking) {
		return bookingClient.createBooking(bookerId, booking);
	}

	@PatchMapping("/{booking-id}")
	public ResponseEntity<Object> updateStatusOfBooking(@PathVariable(name = "booking-id") long bookingId,
														@RequestHeader(value = headerCurrentUser) long ownerId,
														@RequestParam boolean approved) {
		return bookingClient.updateStatusOfBooking(bookingId, ownerId, approved);
	}
}
