package ru.practicum.ewm.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.admin.service.AdminService;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.model.EventStatus;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/events")
    public Collection<EventFullDto> getAllEvents(@RequestParam List<Long> users,
                                                 @RequestParam List<EventStatus> states,
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
        log.info("Get all events by parameters {}", parameters);
        return adminService.findAllEvents(parameters);
    }

    @PutMapping("/events/{eventId}")
    public EventFullDto putEvent(@PathVariable long eventId,
                                 @RequestBody AdminUpdateEventRequest request) {
        log.info("Update event id={}", eventId);
        return adminService.putEvent(eventId, request);
    }

     @PatchMapping("/events/{eventId}/publish")
    public EventFullDto approvePublishEvent(@PathVariable long eventId) {
        log.info("Approve publish event id={}", eventId);
        return adminService.approvePublishEvent(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto approveRejectEvent(@PathVariable long eventId) {
        log.info("Approve reject event id={}", eventId);
        return adminService.approveRejectEvent(eventId);
    }

    @PatchMapping("/categories")
    public CategoryDto patchCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Patch category");
        return adminService.patchCategory(categoryDto);
    }

    @PostMapping("/categories")
    public CategoryDto postCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.info("Post category");
        return adminService.postCategory(newCategoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable long catId) {
        log.info("Delete category");
        adminService.deleteCategory(catId);
    }

    @GetMapping("/users")
    public Collection<UserDto> getAllUsers(@RequestParam List<Long> ids,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Get all users");
        return adminService.findAllUsers(ids, from, size);
    }

    @PostMapping("/users")
    public UserDto postUser(@RequestBody NewUserRequest newUserRequest) {
        log.info("Post user");
        return adminService.postUser(newUserRequest);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Delete user by id={}", userId);
        adminService.deleteUser(userId);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public void pinCompilation(@PathVariable long compId) {
        log.info("Pin compilation id={}", compId);
        adminService.pinCompilation(compId);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public void unpinCompilation(@PathVariable long compId) {
        log.info("Admin unpin compilation id={}", compId);
        adminService.unpinCompilation(compId);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void deleteEventInCompilation(@PathVariable long compId, @PathVariable long eventId) {
        log.info("Delete event id={} in compilation id={}", eventId, compId);
        adminService.deleteEventInCompilation(compId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public void addEventInCompilation(@PathVariable long compId, @PathVariable long eventId) {
        log.info("Add event id={} in compilation id={}", eventId, compId);
        adminService.addEventInCompilation(compId, eventId);
    }

    @PostMapping("/compilations")
    public CompilationDto createCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("Post compilation");
        return adminService.createCompilation(newCompilationDto);
    }

    //TODO ERROR
    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable long compId) {
        log.info("Delete compilation id={}", compId);
        adminService.deleteCompilation(compId);
    }
}
