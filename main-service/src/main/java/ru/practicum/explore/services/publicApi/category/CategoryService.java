package ru.practicum.explore.services.publicApi.category;

import ru.practicum.explore.dto.category.CategoryDto;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс сервиса категорий
 */
public interface CategoryService {
    /*
    Публичный метод контроллера для получения всех категорий
    */
    Collection<CategoryDto> findAll(Integer from, Integer size);

    /*
    Публичный метод контроллера для получения категории по id
    */
    Optional<CategoryDto> getCategoryById(Long catId);
}
