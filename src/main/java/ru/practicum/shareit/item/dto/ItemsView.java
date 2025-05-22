package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemsView {
    Long id;
    String name;
    String description;
    User owner;
    Boolean available;
    LocalDateTime lastBooking;
    LocalDateTime nextBooking;
    List<Comment> comments;
}
