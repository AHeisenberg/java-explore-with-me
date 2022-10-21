package ru.practicum.explore.services.privateApi.location;

import ru.practicum.explore.models.location.Location;

/**
 * Интерфейс сервиса локации
 */
public interface LocationService {
    /*
    Метод сохранения локации
    */
    Location save(Location location);
}
