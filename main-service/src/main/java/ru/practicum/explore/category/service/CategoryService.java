package ru.practicum.explore.category.service;

import ru.practicum.explore.category.dto.CategoryDto;

import java.util.Collection;
import java.util.Optional;

public interface CategoryService {

    Collection<CategoryDto> findAll(int from, int size);

    Optional<CategoryDto> findCategoryById(long catId);
}
