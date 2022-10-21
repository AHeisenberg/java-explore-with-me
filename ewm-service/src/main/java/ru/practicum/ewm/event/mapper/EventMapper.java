package ru.practicum.ewm.event.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.client.stats.StatClient;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.request.model.StatusRequest;
import ru.practicum.ewm.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final ParticipationRequestRepository participationRequestRepository;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final StatClient statClient;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Integer getViews(Long id) {
        String start = LocalDateTime.of(2022, 1, 1, 0, 0, 0).format(FORMATTER);
        String end = LocalDateTime.now().format(FORMATTER);
        ResponseEntity<Object> response = statClient.getStats(start, end, List.of("/events/" + id), false);
        List<LinkedHashMap> collection = (List<LinkedHashMap>) response.getBody();
        if (!collection.isEmpty()) {
            return (Integer) collection.get(0).get("hits");
        }
        return 0;
    }

    public Integer getConfirmedRequests(Long id) {
        return participationRequestRepository.countByEvent_IdAndStatus(id, StatusRequest.CONFIRMED);
    }

    public EventFullDto toEventFullDto(Event event) {
        return event == null ? null :
                EventFullDto.builder()
                        .id(event.getId())
                        .annotation(event.getAnnotation())
                        .category(categoryMapper.toCategoryDto(event.getCategory()))
                        .description(event.getDescription())
                        .eventDate(event.getEventDate().format(FORMATTER))
                        .createdOn(event.getCreatedOn().format(FORMATTER))
                        .initiator(userMapper.toUserShortDto(event.getInitiator()))
                        .location(event.getLocation())
                        .paid(event.getPaid())
                        .participantLimit(event.getParticipantLimit())
                        .confirmedRequests(getConfirmedRequests(event.getId()))
                        .publishedOn(event.getPublishedOn() != null ? event
                                .getPublishedOn()
                                .format(FORMATTER) : null)
                        .requestModeration(event.getRequestModeration())
                        .state(event.getState())
                        .title(event.getTitle())
                        .views(getViews(event.getId()))
                        .build();
    }

    public EventShortDto toEventShortDto(Event event) {
        return event == null ? null :
                EventShortDto.builder()
                        .id(event.getId())
                        .annotation(event.getAnnotation())
                        .category(categoryMapper.toCategoryDto(event.getCategory()))
                        .eventDate(event.getEventDate().format(FORMATTER))
                        .initiator(userMapper.toUserShortDto(event.getInitiator()))
                        .confirmedRequests(getConfirmedRequests(event.getId()))
                        .paid(event.getPaid())
                        .title(event.getTitle())
                        .views(getViews(event.getId()))
                        .build();
    }

    public Event toEvent(NewEventDto newEventDto, User user,
                         Location location, Category category, LocalDateTime eventDate) {
        return newEventDto == null ? null : Event.builder()
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
            event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER));
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
