package ru.practicum.explore.repositories.location;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.models.location.Location;

/**
 * Интерфейс репозитория локаций
 */
public interface LocationRepository extends JpaRepository<Location, Long> {
}
