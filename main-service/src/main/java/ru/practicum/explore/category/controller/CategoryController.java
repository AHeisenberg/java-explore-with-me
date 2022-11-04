package ru.practicum.explore.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.category.service.CategoryService;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        return categoryService.findAll(from, size);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<Object> findCategoryById(@PathVariable Long catId) {
        return categoryService.findCategoryById(catId);
    }
}
