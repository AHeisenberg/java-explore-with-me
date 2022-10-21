package ru.practicum.explore.category.service;

import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;

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
    Optional<CategoryDto> findCategoryById(Long catId);

    /*
Метод контроллера для обновления категории админом
*/
    CategoryDto patchCategory(CategoryDto categoryDto);

    /*
    Метод контроллера для добавления категории админом
    */
    CategoryDto postCategory(NewCategoryDto newCategoryDto);

    /*
    Метод контроллера для удаления категории админом
    */
    void deleteCategory(Long catId);
}
