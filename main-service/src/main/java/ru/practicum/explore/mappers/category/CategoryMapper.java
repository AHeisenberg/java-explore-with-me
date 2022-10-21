package ru.practicum.explore.mappers.category;


import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.category.NewCategoryDto;
import ru.practicum.explore.models.category.Category;

/**
 * Интерфейс маппер категорий
 */
public interface CategoryMapper {
    /*
    метод маппера из котегории в dto категории
    */
    CategoryDto toCategoryDto(Category category);

    /*
    Метод маппера из dto новой категории в категорию
    */
    Category toCategory(NewCategoryDto newCategoryDto);

    /*
    Метод маппера по обновлению категорий
    */
    void updateCategoryFromCategoryDto(CategoryDto categoryDto, Category category);
}
