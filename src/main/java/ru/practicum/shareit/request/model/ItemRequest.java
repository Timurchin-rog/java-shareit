package ru.practicum.shareit.request.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequest {
    Long id;
    String description;
    Long requestorId;
    Date createdTime;
}
