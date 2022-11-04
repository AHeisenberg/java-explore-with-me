package ru.practicum.explore.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.client.EndpointHit;
import ru.practicum.explore.client.stats.StatsClient;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.validator.CommonValidator;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private static final String FROM = "from";
    private static final String SIZE = "size";
    private static final String TEXT = "text";
    private static final String SORT = "sort";
    private static final String CATEGORIES = "categories";
    private static final String PAID = "paid";
    private static final String RANGE_START = "rangeStart";
    private static final String RANGE_END = "rangeEnd";
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;
    private final CommonValidator commonValidator;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public ResponseEntity<Object> findAllEvents(Map<String, Object> parameters) {
        log.info("Find all events by parameters {}", parameters);
        Pageable pageable = PageRequest.of((Integer) parameters.get(FROM) / (Integer) parameters.get(SIZE),
                (Integer) parameters.get(SIZE));
        String text = (String) parameters.get(TEXT);
        List<Long> categories = (List<Long>) parameters.get(CATEGORIES);
        Boolean paid = (Boolean) parameters.get(PAID);
        LocalDateTime rangeStart = LocalDateTime.parse((String) parameters.get(RANGE_START), FORMATTER);
        LocalDateTime rangeEnd = LocalDateTime.parse((String) parameters.get(RANGE_END), FORMATTER);
        Collection<Event> listEvent =
                eventRepository.findAllEventsByParameters(text, categories, paid, rangeStart, rangeEnd, pageable);
        Collection<EventShortDto> listEventShort = listEvent.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        if (!"EVENT_DATE".equals(parameters.get(SORT))) {
            listEventShort.stream().sorted(Comparator.comparing(EventShortDto::getEventDate));
        }
        if (!"VIEWS".equals(parameters.get(SORT))) {
            listEventShort.stream().sorted(Comparator.comparing(EventShortDto::getViews));
        }
        return ResponseEntity.ok(listEventShort);
    }

    @Override
    public ResponseEntity<Object> findEventById(Long id) {
        log.info("Find event by id={}", id);
        commonValidator.validateForEvent(id);
        Event event = eventRepository.findById(id).get();
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        return ResponseEntity.ok(Optional.of(eventFullDto));
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
