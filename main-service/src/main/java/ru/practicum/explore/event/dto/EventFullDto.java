package ru.practicum.explore.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.user.dto.UserShortDto;
import ru.practicum.explore.location.model.Location;
import ru.practicum.explore.event.model.EventStatus;

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
    private EventStatus state;
    private String title;
    private Integer views;
}