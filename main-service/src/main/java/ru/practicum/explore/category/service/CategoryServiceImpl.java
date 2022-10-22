package ru.practicum.explore.category.service;

import lombok.RequiredArgsConstructor;
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
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CommonValidator commonValidator;

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
    public Optional<CategoryDto> findCategoryById(Long catId) {
        commonValidator.categoryValidator(catId);
        Category category = categoryRepository.findById(catId).get();
        return Optional.of(categoryMapper.toCategoryDto(category));
    }

    @Override
    public CategoryDto patchCategory(CategoryDto categoryDto) {
        commonValidator.categoryValidator(categoryDto.getId());
        if (categoryRepository.findFirstByName(categoryDto.getName()).isPresent()) {
            throw new ForbiddenRequestException(String.format("Bad name"));
        }
        Category category = categoryRepository.findById(categoryDto.getId()).get();
        categoryMapper.updateCategoryFromCategoryDto(categoryDto, category);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto postCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.toCategory(newCategoryDto);
        CategoryDto categoryDto = categoryMapper.toCategoryDto(categoryRepository.save(category));
        return categoryDto;
    }

    @Override
    public void deleteCategory(Long catId) {
        commonValidator.categoryValidator(catId);
        categoryRepository.deleteById(catId);
    }
}
