package ru.practicum.explore.controllers.privateApi.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.category.NewCategoryDto;
import ru.practicum.explore.dto.event.AdminUpdateEventRequest;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.user.NewUserRequest;
import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.NewCompilationDto;
import ru.practicum.explore.services.privateApi.admin.AdminService;
import ru.practicum.explore.statuses.event.Status;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * API для работы с событиями, категориями, пользователями и подборками событий
 */
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /*
    Метод контроллера для получения админом всех событий по параметрам
    */
    @GetMapping("/events")
    public Collection<EventFullDto> getAllEvents(@RequestParam List<Long> users,
                                                 @RequestParam List<Status> states,
                                                 @RequestParam List<Long> categories,
                                                 @RequestParam String rangeStart,
                                                 @RequestParam String rangeEnd,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        Map<String, Object> parameters = Map.of(
                "users", users,
                "states", states,
                "categories", categories,
                "rangeStart", rangeStart,
                "rangeEnd", rangeEnd,
                "from", from,
                "size", size
        );
        log.info("Admin Get events by parameters {}", parameters);
        return adminService.getAllEvents(parameters);
    }

    /*
    Метод контроллера для обновления события админом
    */
    @PutMapping("/events/{eventId}")
    public EventFullDto putEvent(@PathVariable Long eventId,
                                 @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("Admin Put eventid={}", eventId);
        return adminService.putEvent(eventId, adminUpdateEventRequest);
    }

    /*
    Метод контроллера для подтверждения события
    */
    @PatchMapping("/events/{eventId}/publish")
    public EventFullDto approvePublishEvent(@PathVariable Long eventId) {
        log.info("Admin approve publish eventId={}", eventId);
        return adminService.approvePublishEvent(eventId);
    }

    /*
    Метод контроллера для отклонения события
    */
    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto approveRejectEvent(@PathVariable Long eventId) {
        log.info("Admin approve reject eventId={}", eventId);
        return adminService.approveRejectEvent(eventId);
    }

    /*
    Метод контроллера для обновления категории админом
    */
    @PatchMapping("/categories")
    public CategoryDto patchCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Admin patch category");
        return adminService.patchCategory(categoryDto);
    }

    /*
    Метод контроллера для добавления категории админом
    */
    @PostMapping("/categories")
    public CategoryDto postCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.info("Admin post category");
        return adminService.postCategory(newCategoryDto);
    }

    /*
    Метод контроллера для удаления категории админом
    */
    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Admin delete category");
        adminService.deleteCategory(catId);
    }

    /*
    Метод контроллера для получения все пользователей админом
    */
    @GetMapping("/users")
    public Collection<UserDto> getAllUsers(@RequestParam List<Long> ids,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Admin get all users");
        return adminService.getAllUsers(ids, from, size);
    }

    /*
    Метод контроллера для добавления нового пользователя админом
    */
    @PostMapping("/users")
    public UserDto postUser(@RequestBody NewUserRequest newUserRequest) {
        log.info("Admin post user");
        return adminService.postUser(newUserRequest);
    }

    /*
    Метод контроллера для удаления пользователя админом
    */
    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Admin delete user by id={}", userId);
        adminService.deleteUser(userId);
    }

    /*
    Метод контроллера для добавления подборки событий админом
    */
    @PostMapping("/compilations")
    public CompilationDto createCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("Admin post compilation");
        return adminService.createCompilation(newCompilationDto);
    }

    /*
    Метод контроллера для удаления подборки событий админом
    */
    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Admin delete comtilation id={}", compId);
        adminService.deleteCompilation(compId);
    }

    /*
    Метод контроллера для удаления из подборки события админом
    */
    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void deleteEventInCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Admin delete event id={} in compilation id={}", eventId, compId);
        adminService.deleteEventInCompilation(compId, eventId);
    }

    /*
    Метод контроллера для добавления в подборку события админом
    */
    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public void addEventInCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Admin add event id={} in compilation id={}", eventId, compId);
        adminService.addEventInCompilation(compId, eventId);
    }

    /*
    Метод контроллера закренления подборки событий админом
    */
    @DeleteMapping("/compilations/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        log.info("Admin unpin compilation id={}", compId);
        adminService.unpinCompilation(compId);
    }

    /*
    Метод контроллера для открепления подборки событий админом
    */
    @PatchMapping("/compilations/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.info("Admin pin compilation id={}", compId);
        adminService.pinCompilation(compId);
    }
}
