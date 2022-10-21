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

    Collection<EventShortDto> findAllEventsByUserId(Long userId, Integer from, Integer size);

    EventFullDto patchEventByUser(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto postEvent(Long userId, NewEventDto newEventDto);

    EventFullDto findEventFull(Long userId, Long eventId);

    EventFullDto cancelEventByUser(Long userId, Long eventId);

    Collection<ParticipationRequestDto> findRequestByUser(Long userId, Long eventId);

    ParticipationRequestDto approveConfirmUserByEvent(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto approveRejectUserByEvent(Long userId, Long eventId, Long reqId);

    Collection<ParticipationRequestDto> findRequestsByUser(Long userId);

    ParticipationRequestDto postRequestUser(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId);

    /*
Метод контроллера для получения все пользователей админом
*/
    Collection<UserDto> findAllUsers(List<Long> ids, Integer from, Integer size);

    /*
    Метод контроллера для добавления нового пользователя админом
    */
    UserDto postUser(NewUserRequest newUserRequest);

    /*
    Метод контроллера для удаления пользователя админом
    */
    void deleteUser(Long userId);
}
