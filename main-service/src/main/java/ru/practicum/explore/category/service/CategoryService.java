package ru.practicum.explore.category.service;

import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;

import java.util.Collection;
import java.util.Optional;

public interface CategoryService {

    Collection<CategoryDto> findAll(Integer from, Integer size);

    Optional<CategoryDto> findCategoryById(Long catId);

    CategoryDto patchCategory(CategoryDto categoryDto);

    CategoryDto postCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);
}
