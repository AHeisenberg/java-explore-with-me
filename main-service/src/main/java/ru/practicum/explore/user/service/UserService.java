package ru.practicum.explore.user.service;

import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.dto.UpdateEventRequest;
import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;

import java.util.Collection;
import java.util.List;

public interface UserService {

    Collection<EventShortDto> findAllEventsByUserId(long userId, int from, int size);

    EventFullDto patchEventByUser(long userId, UpdateEventRequest updateEventRequest);

    EventFullDto postEvent(long userId, NewEventDto newEventDto);

    EventFullDto getEventFull(long userId, long eventId);

    EventFullDto cancelEvent(long userId, long eventId);

    Collection<ParticipationRequestDto> getRequestByUser(long userId, long eventId);

    ParticipationRequestDto approveConfirmUserByEvent(long userId, long eventId, long reqId);

    ParticipationRequestDto approveRejectUserByEvent(long userId, long eventId, long reqId);

    Collection<ParticipationRequestDto> getRequestsByUser(long userId);

    ParticipationRequestDto postRequestUser(long userId, long eventId);

    ParticipationRequestDto cancelRequestByUser(long userId, long requestId);

    Collection<UserDto> findAllUsers(List<Long> ids, int from, int size);

    UserDto postUser(NewUserRequest newUserRequest);

    void deleteUser(long userId);

}
