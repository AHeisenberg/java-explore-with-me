package ru.practicum.explore.event.service;

import org.springframework.beans.factory.annotation.Autowired;
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
public class EventServiceImpl implements EventService {
    private EventRepository eventRepository;
    private ParticipationRequestRepository participationRequestRepository;
    private EventMapper eventMapper;
    private StatsClient statsClient;
    private ObjectValidate objectValidate;

//    private CategoryRepository categoryRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            ParticipationRequestRepository participationRequestRepository, EventMapper eventMapper,
                            StatsClient statsClient, ObjectValidate objectValidate) {
        this.eventRepository = eventRepository;
        this.participationRequestRepository = participationRequestRepository;
        this.eventMapper = eventMapper;
        this.statsClient = statsClient;
        this.objectValidate = objectValidate;
    }

    @Override
    public Collection<EventShortDto> getAll(Map<String, Object> parameters) {
        Pageable pageable = PageRequest.of((Integer) parameters.get("from") / (Integer) parameters.get("size"),
                (Integer) parameters.get("size"));
        String text = (String) parameters.get("text");
        List<Long> categories = (List<Long>) parameters.get("categories");
        Boolean paid = (Boolean) parameters.get("paid");
        LocalDateTime rangeStart = LocalDateTime.parse((String) parameters.get("rangeStart"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime rangeEnd = LocalDateTime.parse((String) parameters.get("rangeEnd"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Collection<Event> listEvent =
                eventRepository.getAllEventsByParameters(text, categories, paid, rangeStart, rangeEnd, pageable);
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
    public Optional<EventFullDto> getEvent(Long id) {
        objectValidate.validateEvent(id);
        Event event = eventRepository.findById(id).get();
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        return Optional.of(eventFullDto);
    }

    @Override
    public void saveInStatService(HttpServletRequest request) {
        EndpointHit endpointHit = EndpointHit.builder()
                .app("main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        statsClient.save(endpointHit);
    }


}
