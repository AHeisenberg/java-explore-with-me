package ru.practicum.explore.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.user.UserShortDto;
import ru.practicum.explore.models.location.Location;
import ru.practicum.explore.statuses.event.Status;

/**
 * Dto для получения полной информации о событие
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private String description;
    private Integer confirmedRequests;
    private String createdOn;
    private String eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private Status state;
    private String title;
    private Integer views;
}
