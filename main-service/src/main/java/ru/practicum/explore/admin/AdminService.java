package ru.practicum.explore.admin;

import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.event.dto.AdminUpdateEventRequest;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Интерфейс сервиса пользователей
 */
public interface AdminService {
    /*
    Метод контроллера для получения админом всех событий по параметрам
    */
    Collection<EventFullDto> getAllEvents(Map<String, Object> parameters);

    /*
    Метод контроллера для обновления события админом
    */
    EventFullDto putEvent(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    /*
    Метод контроллера для подтверждения события
    */
    EventFullDto approvePublishEvent(Long eventId);

    /*
    Метод контроллера для отклонения события
    */
    EventFullDto approveRejectEvent(Long eventId);

    /*
    Метод контроллера для обновления категории админом
    */
    CategoryDto patchCategory(CategoryDto categoryDto);

    /*
    Метод контроллера для добавления категории админом
    */
    CategoryDto postCategory(NewCategoryDto newCategoryDto);

    /*
    Метод контроллера для удаления категории админом
    */
    void deleteCategory(Long catId);



    /*
    Метод контроллера для добавления подборки событий админом
    */
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    /*
    Метод контроллера для удаления подборки событий админом
    */
    void deleteCompilation(Long compId);

    /*
    Метод контроллера для удаления из подборки события админом
    */
    void deleteEventInCompilation(Long compId, Long eventId);

    /*
    Метод контроллера для добавления в подборку события админом
    */
    void addEventInCompilation(Long compId, Long eventId);

    /*
     Метод контроллера закренления подборки событий админом
     */
    void unpinCompilation(Long compId);

    /*
    Метод контроллера для открепления подборки событий админом
    */
    void pinCompilation(Long compId);
}
