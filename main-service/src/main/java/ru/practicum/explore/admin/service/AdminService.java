package ru.practicum.explore.admin.service;

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

public interface AdminService {

    Collection<EventFullDto> findAllEvents(Map<String, Object> parameters);

    EventFullDto putEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    EventFullDto approvePublishEvent(long eventId);

    EventFullDto approveRejectEvent(long eventId);

    CategoryDto patchCategory(CategoryDto categoryDto);

    CategoryDto postCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long catId);

    Collection<UserDto> findAllUsers(List<Long> ids, int from, int size);

    UserDto postUser(NewUserRequest newUserRequest);

    void deleteUser(long userId);

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(long compId);

    void deleteEventInCompilation(long compId, long eventId);

    void addEventInCompilation(long compId, long eventId);

    void unpinCompilation(long compId);

    void pinCompilation(long compId);
}
