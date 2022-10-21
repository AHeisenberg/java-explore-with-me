package ru.practicum.explore.models.error;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс ошибки <b>errors</b>,<b>message</b>,<b>reason</b>,<b>timestamp</b>.
 *
 * @version 1.1
 * @autor Дмитрий Бармин
 */
@Data
@Builder
public class ApiError {
    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    private LocalDateTime timestamp;
}

