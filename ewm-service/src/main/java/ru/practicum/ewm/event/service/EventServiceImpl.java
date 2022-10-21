package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.client.stats.EndpointHit;
import ru.practicum.ewm.client.stats.StatClient;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.validator.CommonValidator;

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
    private final StatClient statClient;
    private final CommonValidator commonValidator;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Collection<EventShortDto> findAllEvents(Map<String, Object> parameters) {
        Pageable pageable = PageRequest.of((Integer) parameters.get("from") / (Integer) parameters.get("size"),
                (Integer) parameters.get("size"));
        String text = String.valueOf(parameters.get("text"));
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
    public Optional<EventFullDto> findEventById(long id) {
        commonValidator.eventValidator(id);
        Event event = eventRepository.findById(id).get();
        return Optional.of(eventMapper.toEventFullDto(event));
    }

    @Override
    public void saveHitInStatsService(HttpServletRequest request) {
        EndpointHit endpointHit = EndpointHit.builder()
                .app("main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();
        statClient.createHit(endpointHit);
    }
}
