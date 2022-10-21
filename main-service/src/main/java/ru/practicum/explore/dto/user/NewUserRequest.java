package ru.practicum.explore.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto обновления пользователя
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {
    private String name;
    private String email;
}
