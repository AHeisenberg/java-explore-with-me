package ru.practicum.explore.event.mapper;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.explore.category.mapper.CategoryMapper;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.client.stats.StatsClient;
import ru.practicum.explore.comment.mapper.CommentMapper;
import ru.practicum.explore.comment.repository.CommentRepository;
import ru.practicum.explore.event.dto.AdminUpdateEventRequest;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.location.model.Location;
import ru.practicum.explore.user.mapper.UserMapper;
import ru.practicum.explore.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    private final StatsClient statsClient;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventFullDto toEventFullDto(@NotNull Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .eventDate(event.getEventDate().format(FORMATTER))
                .paid(event.getPaid())
                .title(event.getTitle())
                .createdOn(event.getCreatedOn().format(FORMATTER))
                .description(event.getDescription())
                .location(event.getLocation())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn() == null ? LocalDateTime.now().format(FORMATTER) : event.getPublishedOn()
                        .format(FORMATTER))
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .views(getViews(event.getId()))
                .comments(commentRepository.findAllByEventOrderByCreated(event).stream()
                        .map(commentMapper::toCommentDto).collect(Collectors.toList()))
                .build();
    }


    public EventShortDto toEventShortDto(@NotNull Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate().format(FORMATTER))
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(getViews(event.getId()))
                .comments(commentRepository.findAllByEventOrderByCreated(event).stream()
                        .map(commentMapper::toCommentDto).collect(Collectors.toList()))
                .build();
    }


    public Event toEvent(@NotNull NewEventDto newEventDto, User user, Location location,
                         Category category, LocalDateTime eventDate) {
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

    public void updateEventFromAdminUpdateEventRequest(
            @NotNull AdminUpdateEventRequest adminUpdateEventRequest, Event event) {
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


    public Integer getViews(Long id) {
        String start = LocalDateTime.of(2020, 1, 1, 0, 0, 0).format(FORMATTER);
        String end = LocalDateTime.now().format(FORMATTER);
        ResponseEntity<Object> response = statsClient.getStats(start, end, List.of("/events/" + id), false);
        List<LinkedHashMap> collection = (List<LinkedHashMap>) response.getBody();
        return collection.isEmpty() ? 0 : (Integer) collection.get(0).get("hits");
    }

}
