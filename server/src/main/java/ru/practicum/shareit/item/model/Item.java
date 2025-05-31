package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {

    public Item(String name, String description, User owner, Boolean available) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.available = available;
    }

    public Item(Long id, String name, String description, User owner, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.available = available;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    String description;

    @ManyToOne
    User owner;

    @Column(nullable = false)
    Boolean available;

    @OneToOne
    Request request;
}
