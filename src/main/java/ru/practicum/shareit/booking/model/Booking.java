package ru.practicum.shareit.booking.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    Long id;
    Long itemId;
    Date startOfBooking;
    Date endOfBooking;
    Long bookerId;
    Status status;
}
