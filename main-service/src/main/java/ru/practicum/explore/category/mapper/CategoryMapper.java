package ru.practicum.explore.category.mapper;

import com.sun.istack.NotNull;
import org.springframework.stereotype.Component;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;
import ru.practicum.explore.category.model.Category;

@Component
public class CategoryMapper {
    public CategoryDto toCategoryDto(@NotNull Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category toCategory(@NotNull NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

    public void updateCategoryFromCategoryDto(@NotNull CategoryDto categoryDto, @NotNull Category category) {
        if (categoryDto.getId() != null) {
            category.setId(category.getId());
        }
        if (categoryDto.getName() != null) {
            category.setName(categoryDto.getName());
        }
    }
}
