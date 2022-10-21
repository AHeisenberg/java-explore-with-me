package ru.practicum.explore.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.models.location.Location;

/**
 * Dto для добавления события
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    private String annotation;
    private Integer category;
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
