package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface EventService {

    Collection<EventShortDto> findAllEvents(Map<String, Object> parameters);

    Optional<EventFullDto> findEventById(long id);

    void saveHitInStatsService(HttpServletRequest request);
}
