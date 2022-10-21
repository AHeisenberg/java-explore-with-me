package ru.practicum.explore.repositories.compilation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.models.compilation.Compilation;

import java.util.List;

/**
 * Интерфейс репозитория подборок событий
 */
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    /*
    Метод получения закрепленных подборок
    */
    @Query("select c from Compilation c where c.pinned=?1 ORDER BY c.id")
    List<Compilation> findAllByPinnedOrderById(Boolean pinned, Pageable pageable);
}
