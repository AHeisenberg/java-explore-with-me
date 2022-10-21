package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Collection<CategoryDto> findAll(@RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Find all category's");
        return categoryService.findAll(from, size);
    }

    @GetMapping("/{catId}")
    public Optional<CategoryDto> findCategoryById(@PathVariable long catId) {
        log.info("Find category by id={}", catId);
        return categoryService.findCategoryById(catId);
    }
}
