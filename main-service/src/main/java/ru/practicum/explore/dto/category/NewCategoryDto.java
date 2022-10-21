package ru.practicum.explore.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для добавление новой категории
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {
    private String name;
}
