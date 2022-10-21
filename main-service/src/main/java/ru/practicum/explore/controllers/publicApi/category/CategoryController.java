package ru.practicum.explore.controllers.publicApi.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.services.publicApi.category.CategoryService;

import java.util.Collection;
import java.util.Optional;

/**
 * Публичный API для работы с категориями
 */
@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /*
    Публичный метод контроллера для получения всех категорий
    */
    @GetMapping
    public Collection<CategoryDto> findAll(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Find all category");
        return categoryService.findAll(from, size);
    }

    /*
    Публичный метод контроллера для получения категории по id
    */
    @GetMapping("/{catId}")
    public Optional<CategoryDto> getCategoryById(@PathVariable Long catId) {
        log.info("Get Category id={}", catId);
        return categoryService.getCategoryById(catId);
    }
}
