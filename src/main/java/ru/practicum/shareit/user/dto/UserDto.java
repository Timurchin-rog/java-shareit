package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    @Email
    String email;
    String name;

    public boolean hasEmail() {
        return ! (email == null || email.isBlank());
    }

    public boolean hasName() {
        return ! (name == null || name.isBlank());
    }
}
