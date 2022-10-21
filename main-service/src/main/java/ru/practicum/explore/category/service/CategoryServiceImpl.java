package ru.practicum.explore.category.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.validator.ObjectValidate;
import ru.practicum.explore.category.mapper.CategoryMapper;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.category.repository.CategoryRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;
    private ObjectValidate objectValidate;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper,
                               ObjectValidate objectValidate) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.objectValidate = objectValidate;
    }

    @Override
    public Collection<CategoryDto> findAll(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Category> categoryCollection = categoryRepository.findAll(pageable);
        Collection<CategoryDto> listCategoryDto = categoryCollection.stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
        return listCategoryDto;
    }

    @Override
    public Optional<CategoryDto> getCategoryById(Long catId) {
        objectValidate.validateCategory(catId);
        Category category = categoryRepository.findById(catId).get();
        return Optional.of(categoryMapper.toCategoryDto(category));
    }
}
