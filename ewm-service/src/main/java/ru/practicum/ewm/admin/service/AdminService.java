package ru.practicum.ewm.admin.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

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

    void pinCompilation(long compId);

    void unpinCompilation(long compId);

    void deleteEventInCompilation(long compId, long eventId);

    void addEventInCompilation(long compId, long eventId);

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(long compId);
}
