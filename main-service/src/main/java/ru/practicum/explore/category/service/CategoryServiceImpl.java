package ru.practicum.explore.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.mapper.CategoryMapper;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.category.repository.CategoryRepository;
import ru.practicum.explore.validator.CommonValidator;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CommonValidator commonValidator;

    @Override
    public Collection<CategoryDto> findAll(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Category> categoryCollection = categoryRepository.findAll(pageable);
        return categoryCollection.stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryDto> findCategoryById(long catId) {
        commonValidator.categoryValidator(catId);
        return Optional.of(categoryMapper.toCategoryDto(categoryRepository.findById(catId).get()));
    }
}