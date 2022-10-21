package ru.practicum.explore.admin;

import ru.practicum.explore.event.dto.AdminUpdateEventRequest;
import ru.practicum.explore.event.dto.EventFullDto;

import java.util.Collection;
import java.util.Map;

/**
 * Интерфейс сервиса пользователей
 */
public interface AdminService {

    /*
Метод контроллера для получения админом всех событий по параметрам
*/
    Collection<EventFullDto> getAllEvents(Map<String, Object> parameters);

    /*
    Метод контроллера для обновления события админом
    */
    EventFullDto putEvent(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    /*
    Метод контроллера для подтверждения события
    */
    EventFullDto approvePublishEvent(Long eventId);

    /*
    Метод контроллера для отклонения события
    */
    EventFullDto approveRejectEvent(Long eventId);


}
