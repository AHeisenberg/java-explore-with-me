package ru.practicum.explore.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;
import ru.practicum.explore.category.service.CategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PatchMapping
    public ResponseEntity<Object> patchCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.patchCategory(categoryDto);
    }

    @PostMapping
    public ResponseEntity<Object> postCategory(@RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.postCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long catId) {
        return categoryService.deleteCategory(catId);
    }


}
