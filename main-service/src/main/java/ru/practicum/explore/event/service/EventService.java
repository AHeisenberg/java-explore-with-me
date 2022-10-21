package ru.practicum.explore.event.service;

import ru.practicum.explore.event.dto.AdminUpdateEventRequest;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface EventService {

    Collection<EventShortDto> findAllEvents(Map<String, Object> parameters);

    Optional<EventFullDto> findEventById(long id);

    void saveHitInStatsService(HttpServletRequest request);

    Collection<EventFullDto> findAllEvents4Admin(Map<String, Object> parameters);

    EventFullDto putEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    EventFullDto approvePublishEvent(long eventId);

    EventFullDto approveRejectEvent(long eventId);
}
