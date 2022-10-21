package ru.practicum.explore.mappers.event;

import ru.practicum.explore.dto.event.*;
import ru.practicum.explore.models.category.Category;
import ru.practicum.explore.models.event.Event;
import ru.practicum.explore.models.location.Location;
import ru.practicum.explore.models.user.User;

import java.time.LocalDateTime;

/**
 * Интерфейс маппера событий
 */
public interface EventMapper {
    /*
    Метод маппера из модели события в dto полной изформации о событие
     */
    EventFullDto toEventFullDto(Event event);

    /*
    Метод маппера из модели события в dto  краткой информации о событие
     */
    EventShortDto toEventShortDto(Event event);

    /*
    Метод маппера по добовлению новой модели события
     */
    Event toEvent(NewEventDto newEventDto, User user, Location location, Category category, LocalDateTime eventDate);

    /*
    Метод маппера обновления модели события пользователем
     */
    void updateEventFromNewEventDto(UpdateEventRequest updateEventRequest, Event event);

    /*
    Метод маппера обновление модели события админом
     */
    void updateEventFromAdminUpdateEventRequest(AdminUpdateEventRequest adminUpdateEventRequest, Event event);
}
