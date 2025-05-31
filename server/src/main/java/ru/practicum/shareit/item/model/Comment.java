package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {

    public Comment(String text, User author, Item item, LocalDateTime created) {
        this.text = text;
        this.author = author;
        this.item = item;
        this.created = created;
    }

    public Comment(Long id, String text, User author, Item item) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.item = item;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String text;

    @ManyToOne
    User author;

    @ManyToOne
    Item item;

    @Column(nullable = false)
    LocalDateTime created;
}
