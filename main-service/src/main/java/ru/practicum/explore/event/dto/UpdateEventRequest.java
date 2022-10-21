package ru.practicum.explore.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {

    private Long eventId;

    private String annotation;

    private String title;

    private Integer category;

    private String description;

    private String eventDate;

    private Integer participantLimit;

    private Boolean paid;
}
