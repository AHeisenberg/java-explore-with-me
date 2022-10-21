package ru.practicum.explore.services.privateApi.user;

import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.dto.event.NewEventDto;
import ru.practicum.explore.dto.event.UpdateEventRequest;
import ru.practicum.explore.dto.request.ParticipationRequestDto;

import java.util.Collection;

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
}
