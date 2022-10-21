package ru.practicum.explore.mappers.event;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.explore.dto.event.*;
import ru.practicum.explore.mappers.category.CategoryMapper;
import ru.practicum.explore.models.category.Category;
import ru.practicum.explore.clients.stat.StatClient;
import ru.practicum.explore.models.location.Location;
import ru.practicum.explore.models.event.Event;
import ru.practicum.explore.repositories.request.ParticipationRequestRepository;
import ru.practicum.explore.statuses.request.StatusRequest;
import ru.practicum.explore.mappers.user.UserMapper;
import ru.practicum.explore.models.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;

@Component
public class EventMapperImpl implements EventMapper {
    private CategoryMapper categoryMapper;
    private UserMapper userMapper;
    private StatClient statClient;
    private ParticipationRequestRepository participationRequestRepository;

    public EventMapperImpl(CategoryMapper categoryMapper, UserMapper userMapper, StatClient statClient,
                           ParticipationRequestRepository participationRequestRepository) {
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
        this.statClient = statClient;
        this.participationRequestRepository = participationRequestRepository;
    }

    public Integer getViews(Long id) {
        String start = LocalDateTime.of(2000, 1, 1, 0, 0, 0)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ResponseEntity<Object> response = statClient.getStats(start, end, List.of("/events/" + id), false);
        List<LinkedHashMap> collection = (List<LinkedHashMap>) response.getBody();
        if (!collection.isEmpty()) {
            Integer views = (Integer) collection.get(0).get("hits");
            return views;
        }
        return 0;
    }

    public Integer getConfirmedRequests(Long id) {
        Integer limitParticipant = participationRequestRepository.countByEvent_IdAndStatus(id, StatusRequest.CONFIRMED);
        return limitParticipant;
    }

    @Override
    public EventFullDto toEventFullDto(Event event) {
        if (event == null) {
            return null;
        }
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .createdOn(event.getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .confirmedRequests(getConfirmedRequests(event.getId()))
                .publishedOn(event.getPublishedOn() != null ? event
                        .getPublishedOn()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null)
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(getViews(event.getId()))
                .build();
    }

    @Override
    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .confirmedRequests(getConfirmedRequests(event.getId()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(getViews(event.getId()))
                .build();
    }

    @Override
    public Event toEvent(NewEventDto newEventDto, User user,
                         Location location, Category category, LocalDateTime eventDate) {
        if (newEventDto == null) {
            return null;
        }
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .description(newEventDto.getDescription())
                .createdOn(LocalDateTime.now())
                .eventDate(eventDate)
                .location(location)
                .initiator(user)
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }

    @Override
    public void updateEventFromNewEventDto(UpdateEventRequest newEventDto, Event event) {
        if (newEventDto == null) {
            return;
        }
        if (newEventDto.getAnnotation() != null) {
            event.setAnnotation(newEventDto.getAnnotation());
        }
        if (newEventDto.getDescription() != null) {
            event.setDescription(newEventDto.getDescription());
        }
        if (newEventDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if (newEventDto.getPaid() != null) {
            event.setPaid(newEventDto.getPaid());
        }
        if (newEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(newEventDto.getParticipantLimit());
        }
        if (newEventDto.getTitle() != null) {
            event.setTitle(newEventDto.getTitle());
        }
    }

    @Override
    public void updateEventFromAdminUpdateEventRequest(AdminUpdateEventRequest adminUpdateEventRequest, Event event) {
        if (adminUpdateEventRequest.getAnnotation() != null) {
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        if (adminUpdateEventRequest.getDescription() != null) {
            event.setDescription(adminUpdateEventRequest.getDescription());
        }
        if (adminUpdateEventRequest.getLocation() != null) {
            event.setLocation(adminUpdateEventRequest.getLocation());
        }
        if (adminUpdateEventRequest.getPaid() != null) {
            event.setPaid(adminUpdateEventRequest.getPaid());
        }
        if (adminUpdateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        }
        if (adminUpdateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        }
        if (adminUpdateEventRequest.getTitle() != null) {
            event.setTitle(adminUpdateEventRequest.getTitle());
        }
    }
}
