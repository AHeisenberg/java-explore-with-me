package ru.practicum.explore.services.publicApi.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.errors.validate.ObjectValidate;
import ru.practicum.explore.clients.stat.StatClient;
import ru.practicum.explore.dto.client.EndpointHit;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.mappers.event.EventMapper;
import ru.practicum.explore.models.event.Event;
import ru.practicum.explore.repositories.event.EventRepository;
import ru.practicum.explore.repositories.request.ParticipationRequestRepository;

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
    private StatClient statClient;
    private ObjectValidate objectValidate;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            ParticipationRequestRepository participationRequestRepository, EventMapper eventMapper,
                            StatClient statClient, ObjectValidate objectValidate) {
        this.eventRepository = eventRepository;
        this.participationRequestRepository = participationRequestRepository;
        this.eventMapper = eventMapper;
        this.statClient = statClient;
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
        statClient.save(endpointHit);
    }
}
