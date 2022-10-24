package ru.practicum.explore.admin;

import lombok.RequiredArgsConstructor;
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
    public CategoryDto patchCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.patchCategory(categoryDto);
    }

    @PostMapping
    public CategoryDto postCategory(@RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.postCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }


}
