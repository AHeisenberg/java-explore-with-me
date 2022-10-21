package ru.practicum.explore.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.category.repository.CategoryRepository;
import ru.practicum.explore.event.dto.AdminUpdateEventRequest;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.EventStatus;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.exc.ForbiddenRequestException;
import ru.practicum.explore.exc.ObjectNotFoundException;
import ru.practicum.explore.validator.CommonValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CommonValidator commonValidator;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public Collection<EventFullDto> findAllEvents(Map<String, Object> parameters) {
        Pageable pageable = PageRequest.of((Integer) parameters.get("from") / (Integer) parameters.get("size"),
                (Integer) parameters.get("size"));
        List<Long> users = (List<Long>) parameters.get("users");
        List<EventStatus> states = (List<EventStatus>) parameters.get("states");
        List<Long> catIds = (List<Long>) parameters.get("categories");
        LocalDateTime rangeStart = LocalDateTime.parse((String) parameters.get("rangeStart"), FORMATTER);
        LocalDateTime rangeEnd = LocalDateTime.parse((String) parameters.get("rangeEnd"), FORMATTER);
        return eventRepository.findAllEvents(users, states, catIds, rangeStart, rangeEnd, pageable).stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto putEvent(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        commonValidator.validateEvent(eventId);
        LocalDateTime eventDate = LocalDateTime.parse(adminUpdateEventRequest.getEventDate(), FORMATTER);
        if (!eventDate.isAfter(LocalDateTime.now().minusHours(2))) {
            throw new ForbiddenRequestException(String.format("Bad date."));
        }
        Event event = eventRepository.findById(eventId).get();
        if (adminUpdateEventRequest.getCategory() != null) {
            if (categoryRepository.findById(Long.valueOf(adminUpdateEventRequest.getCategory())).isEmpty()) {
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
        commonValidator.validateEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        event.setState(EventStatus.PUBLISHED);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto approveRejectEvent(Long eventId) {
        commonValidator.validateEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        event.setState(EventStatus.CANCELED);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }
}
