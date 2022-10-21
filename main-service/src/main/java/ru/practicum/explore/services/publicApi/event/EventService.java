package ru.practicum.explore.services.publicApi.event;

import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Интерфейс сервиса события
 */
public interface EventService {
    /*
    Получение событий с возможностью фильтрации
    */
    Collection<EventShortDto> getAll(Map<String, Object> parameters);

    /*
    Получение подробной информации об опубликованном событии по его идентификатору
    */
    Optional<EventFullDto> getEvent(Long id);

    /*
    Метод отправки ендпоинта на сервис статистики
    */
    void saveInStatService(HttpServletRequest request);
}
