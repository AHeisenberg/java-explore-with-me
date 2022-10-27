package ru.practicum.explore.category.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;

public interface CategoryService {

    ResponseEntity<Object> findAll(Integer from, Integer size);

    ResponseEntity<Object> findCategoryById(Long catId);

    ResponseEntity<Object> patchCategory(CategoryDto categoryDto);

    ResponseEntity<Object> postCategory(NewCategoryDto newCategoryDto);

    ResponseEntity<Object> deleteCategory(Long catId);
}
