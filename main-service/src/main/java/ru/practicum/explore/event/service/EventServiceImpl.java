package ru.practicum.explore.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.category.repository.CategoryRepository;
import ru.practicum.explore.clients.EndpointHit;
import ru.practicum.explore.clients.stat.StatClient;
import ru.practicum.explore.event.dto.AdminUpdateEventRequest;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.EventStatus;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.exc.ForbiddenRequestException;
import ru.practicum.explore.exc.ObjectNotFoundException;
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
    private StatClient statClient;
    private ObjectValidate objectValidate;

    private CategoryRepository categoryRepository;

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

    @Override
    public Collection<EventFullDto> getAllEvents(Map<String, Object> parameters) {
        Pageable pageable = PageRequest.of((Integer) parameters.get("from") / (Integer) parameters.get("size"),
                (Integer) parameters.get("size"));
        List<Long> users = (List<Long>) parameters.get("users");
        List<EventStatus> states = (List<EventStatus>) parameters.get("states");
        List<Long> catIds = (List<Long>) parameters.get("categories");
        LocalDateTime rangeStart = LocalDateTime.parse((String) parameters.get("rangeStart"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime rangeEnd = LocalDateTime.parse((String) parameters.get("rangeEnd"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Collection<EventFullDto> fullDtoCollection =
                eventRepository.getAllEvents(users, states, catIds, rangeStart, rangeEnd, pageable).stream()
                        .map(eventMapper::toEventFullDto)
                        .collect(Collectors.toList());
        return fullDtoCollection;
    }

    @Override
    public EventFullDto putEvent(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        objectValidate.validateEvent(eventId);
        LocalDateTime eventDate = LocalDateTime.parse(adminUpdateEventRequest.getEventDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (!eventDate.isAfter(LocalDateTime.now().minusHours(2))) {
            throw new ForbiddenRequestException(String.format("Bad date."));
        }
        Event event = eventRepository.findById(eventId).get();
        if (adminUpdateEventRequest.getCategory() != null) {
            if (!categoryRepository.findById(Long.valueOf(adminUpdateEventRequest.getCategory())).isPresent()) {
                throw new ObjectNotFoundException(String.format("Category not found."));
            }
            Category category = categoryRepository.findById(Long.valueOf(adminUpdateEventRequest.getCategory())).get();
            event.setCategory(category);
            event.setEventDate(eventDate);
        }
        eventMapper.updateEventFromAdminUpdateEventRequest(adminUpdateEventRequest, event);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto approvePublishEvent(Long eventId) {
        objectValidate.validateEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        event.setState(EventStatus.PUBLISHED);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto approveRejectEvent(Long eventId) {
        objectValidate.validateEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        event.setState(EventStatus.CANCELED);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }
}
