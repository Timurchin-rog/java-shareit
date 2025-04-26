package ru.practicum.shareit.item.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    Long id;
    String name;
    String description;
    Long ownerId;
    Boolean available;
    Set<Review> reviews;
}
