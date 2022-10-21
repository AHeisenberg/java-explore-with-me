package ru.practicum.explore.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.service.CategoryService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Collection<CategoryDto> findAll(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Find all category's");
        return categoryService.findAll(from, size);
    }

    @GetMapping("/{catId}")
    public Optional<CategoryDto> findCategoryById(@PathVariable Long catId) {
        log.info("Find Category id={}", catId);
        return categoryService.findCategoryById(catId);
    }
}
