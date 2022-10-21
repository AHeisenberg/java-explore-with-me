package ru.practicum.explore.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.client.EndpointHit;
import ru.practicum.explore.client.stats.StatsClient;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.request.repository.ParticipationRequestRepository;
import ru.practicum.explore.validator.ObjectValidate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;
    private final ObjectValidate objectValidate;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Collection<EventShortDto> findAllEvents(Map<String, Object> parameters) {
        Pageable pageable = PageRequest.of((Integer) parameters.get("from") / (Integer) parameters.get("size"),
                (Integer) parameters.get("size"));
        String text = (String) parameters.get("text");
        List<Long> categories = (List<Long>) parameters.get("categories");
        Boolean paid = (Boolean) parameters.get("paid");
        LocalDateTime rangeStart = LocalDateTime.parse((String) parameters.get("rangeStart"), FORMATTER);
        LocalDateTime rangeEnd = LocalDateTime.parse((String) parameters.get("rangeEnd"), FORMATTER);
        Collection<Event> listEvent =
                eventRepository.findAllEventsByParameters(text, categories, paid, rangeStart, rangeEnd, pageable);
        Collection<EventShortDto> listEventShort = listEvent.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        if (parameters.get("sort").equals("EVENT_DATE")) {
            listEventShort.stream().sorted(Comparator.comparing(EventShortDto::getEventDate));
        }
        if (parameters.get("sort").equals("VIEWS")) {
            listEventShort.stream().sorted(Comparator.comparing(EventShortDto::getViews));
        }
        return listEventShort;
    }

    @Override
    public Optional<EventFullDto> findEventById(Long id) {
        objectValidate.validateEvent(id);
        Event event = eventRepository.findById(id).get();
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        return Optional.of(eventFullDto);
    }

    @Override
    public void saveHitInStatsService(HttpServletRequest request) {
        EndpointHit endpointHit = EndpointHit.builder()
                .app("main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
        statsClient.save(endpointHit);
    }
}
