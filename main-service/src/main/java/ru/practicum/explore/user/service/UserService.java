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

/**
 * Интерфейс сервиса пользователя
 */
public interface UserService {
    /*
    Метод контроллера для получение событий, добавленных текущим пользователем.
    */
    Collection<EventShortDto> findAllEventsByUserId(Long userId, Integer from, Integer size);

    /*
    Метод контроллера для изменение события добавленного текущим пользователем.
    */
    EventFullDto patchEventByUser(Long userId, UpdateEventRequest updateEventRequest);

    /*
    Метод контроллера для добавление нового события.
    */
    EventFullDto postEvent(Long userId, NewEventDto newEventDto);

    /*
    Получение полной информации о событии добавленном текущим пользователем.
    */
    EventFullDto getEventFull(Long userId, Long eventId);

    /*
    Отмена события добавленного текущим пользователем.
    */
    EventFullDto cancelEvent(Long userId, Long eventId);

    /*
    Получение информации о запросах на участие в событии текущего пользователя.
    */
    Collection<ParticipationRequestDto> getRequestByUser(Long userId, Long eventId);

    /*
    Подтверждение чужой заявки на участие в событии текущего пользователя
    */
    ParticipationRequestDto approveConfirmUserByEvent(Long userId, Long eventId, Long reqId);

    /*
    Отклонение чужой заявки на участие в событии текущего пользователя.
    */
    ParticipationRequestDto approveRejectUserByEvent(Long userId, Long eventId, Long reqId);

    /*
    Получение информации о заявках текущего пользователя на участие в чужих событиях.
    */
    Collection<ParticipationRequestDto> getRequestsByUser(Long userId);

    /*
    Добавление запроса от текущего пользователя на участие в событии.
    */
    ParticipationRequestDto postRequestUser(Long userId, Long eventId);

    /*
    Отмена своего запроса на участие в событии.
    */
    ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId);

    /*
Метод контроллера для получения все пользователей админом
*/
    Collection<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size);

    /*
    Метод контроллера для добавления нового пользователя админом
    */
    UserDto postUser(NewUserRequest newUserRequest);

    /*
    Метод контроллера для удаления пользователя админом
    */
    void deleteUser(Long userId);
}
