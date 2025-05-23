package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {

    public Booking(Item item, LocalDateTime start, LocalDateTime end, User booker, Status status) {
        this.item = item;
        this.start = start;
        this.end = end;
        this.booker = booker;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Item item;

    @Column(name = "start_booking")
    LocalDateTime start;

    @Column(name = "end_booking")
    LocalDateTime end;

    @OneToOne
    User booker;

    @Enumerated(EnumType.STRING)
    Status status;
}
