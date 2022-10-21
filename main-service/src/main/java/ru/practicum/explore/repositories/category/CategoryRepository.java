package ru.practicum.explore.repositories.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.models.category.Category;

import java.util.Optional;

/**
 * Интерфейс репозитория категорий
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /*
    Метод валидации имени категории
    */
    @Query("select c from Category c where c.name=?1 order by c.id")
    Optional<Category> findFirstByName(String name);
}
