package ru.practicum.explore.event.service;

import ru.practicum.explore.event.dto.AdminUpdateEventRequest;
import ru.practicum.explore.event.dto.EventFullDto;

import java.util.Collection;
import java.util.Map;

public interface AdminEventService {

    Collection<EventFullDto> findAllEvents(Map<String, Object> parameters);

    EventFullDto putEvent(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    EventFullDto approvePublishEvent(Long eventId);

    EventFullDto approveRejectEvent(Long eventId);


}
