package ru.practicum.explore.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;
import ru.practicum.explore.category.mapper.CategoryMapper;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.category.repository.CategoryRepository;
import ru.practicum.explore.exc.ForbiddenRequestException;
import ru.practicum.explore.validator.CommonValidator;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CommonValidator commonValidator;

    @Override
    public Collection<CategoryDto> findAll(Integer from, Integer size) {
        log.info("Admin find all categories");
        Pageable pageable = PageRequest.of(from / size, size);
        Page<Category> categoryCollection = categoryRepository.findAll(pageable);
        return categoryCollection.stream()
                .map(categoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryDto> findCategoryById(Long catId) {
        log.info("Admin find category by id={}", catId);
        commonValidator.categoryValidator(catId);
        Category category = categoryRepository.findById(catId).get();
        return Optional.of(categoryMapper.toCategoryDto(category));
    }

    @Override
    public CategoryDto patchCategory(CategoryDto categoryDto) {
        log.info("Admin patch category={}", categoryDto.getName());
        commonValidator.categoryValidator(categoryDto.getId());
        if (categoryRepository.findFirstByName(categoryDto.getName()).isPresent()) {
            throw new ForbiddenRequestException("Name is invalid");
        }
        Category category = categoryRepository.findById(categoryDto.getId()).get();
        categoryMapper.updateCategoryFromCategoryDto(categoryDto, category);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto postCategory(NewCategoryDto newCategoryDto) {
        log.info("Admin post category={}", newCategoryDto.getName());
        Category category = categoryMapper.toCategory(newCategoryDto);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long catId) {
        log.info("Admin delete category by id={}", catId);
        commonValidator.categoryValidator(catId);
        categoryRepository.deleteById(catId);
    }
}
