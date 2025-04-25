package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Review;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    Long id;
    String name;
    String description;
    Long ownerId;
    Boolean available;
    Set<Review> reviews;

    public boolean hasName() {
        return ! (name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return ! (description == null || description.isBlank());
    }

    public boolean hasAvailable() {
        return ! (available == null);
    }
}
