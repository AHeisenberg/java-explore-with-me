package ru.practicum.explore.event.service;

import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface EventService {
    /*
    Получение событий с возможностью фильтрации
    */
    Collection<EventShortDto> findAllEvents(Map<String, Object> parameters);

    /*
    Получение подробной информации об опубликованном событии по его идентификатору
    */
    Optional<EventFullDto> getEvent(Long id);

    /*
    Метод отправки ендпоинта на сервис статистики
    */
    void saveInStatService(HttpServletRequest request);

    /*
Метод контроллера для получения админом всех событий по параметрам
*/
//    Collection<EventFullDto> getAllEvents(Map<String, Object> parameters);
//
//    /*
//    Метод контроллера для обновления события админом
//    */
//    EventFullDto putEvent(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);
//
//    /*
//    Метод контроллера для подтверждения события
//    */
//    EventFullDto approvePublishEvent(Long eventId);
//
//    /*
//    Метод контроллера для отклонения события
//    */
//    EventFullDto approveRejectEvent(Long eventId);
}
