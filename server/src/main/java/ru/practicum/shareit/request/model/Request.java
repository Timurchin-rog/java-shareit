package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {

    public Request(String description, User requestor, LocalDateTime created) {
        this.description = description;
        this.requestor = requestor;
        this.created = created;
    }

    public Request(Long id, String description, User requestor) {
        this.id = id;
        this.description = description;
        this.requestor = requestor;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String description;

    @ManyToOne
    User requestor;

    LocalDateTime created;
}
