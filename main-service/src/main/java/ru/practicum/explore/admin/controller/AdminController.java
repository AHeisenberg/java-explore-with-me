package ru.practicum.explore.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.admin.service.AdminService;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.service.CompilationService;
import ru.practicum.explore.event.dto.AdminUpdateEventRequest;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.model.EventStatus;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    private final CompilationService compilationService;

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
        log.info("Admin Get events by parameters {}", parameters);
        return adminService.findAllEvents(parameters);
    }

    @PutMapping("/events/{eventId}")
    public EventFullDto putEvent(@PathVariable long eventId,
                                 @RequestBody AdminUpdateEventRequest request) {
        log.info("Admin Put eventid={}", eventId);
        return adminService.putEvent(eventId, request);
    }

    @PatchMapping("/events/{eventId}/publish")
    public EventFullDto approvePublishEvent(@PathVariable long eventId) {
        log.info("Admin approve publish eventId={}", eventId);
        return adminService.approvePublishEvent(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto approveRejectEvent(@PathVariable long eventId) {
        log.info("Admin approve reject eventId={}", eventId);
        return adminService.approveRejectEvent(eventId);
    }

    @PatchMapping("/categories")
    public CategoryDto patchCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Admin patch category");
        return adminService.patchCategory(categoryDto);
    }

    @PostMapping("/categories")
    public CategoryDto postCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.info("Admin post category");
        return adminService.postCategory(newCategoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable long catId) {
        log.info("Admin delete category");
        adminService.deleteCategory(catId);
    }

    @GetMapping("/users")
    public Collection<UserDto> getAllUsers(@RequestParam List<Long> ids,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Admin get all users");
        return adminService.findAllUsers(ids, from, size);
    }

    @PostMapping("/users")
    public UserDto postUser(@RequestBody NewUserRequest newUserRequest) {
        log.info("Admin post user");
        return adminService.postUser(newUserRequest);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Admin delete user by id={}", userId);
        adminService.deleteUser(userId);
    }

//    @PostMapping("/compilations")
//    public CompilationDto createCompilation(@RequestBody NewCompilationDto newCompilationDto) {
//        log.info("Admin post compilation");
//        return adminService.createCompilation(newCompilationDto);
//    }
//
//    //TODO
//    @DeleteMapping("/compilations/{compId}")
//    public void deleteCompilation(@PathVariable long compId) {
//        log.info("Admin delete compilation id={}", compId);
//        adminService.deleteCompilation(compId);
////        compilationService.findCompilationById(compId);
//
//    }
//
//    @DeleteMapping("/compilations/{compId}/pin")
//    public void unpinCompilation(@PathVariable long compId) {
//        log.info("Admin unpin compilation id={}", compId);
//        adminService.unpinCompilation(compId);
//    }
//
//    @PatchMapping("/compilations/{compId}/pin")
//    public void pinCompilation(@PathVariable long compId) {
//        log.info("Admin pin compilation id={}", compId);
//        adminService.pinCompilation(compId);
//    }
//
//
//    @DeleteMapping("/compilations/{compId}/events/{eventId}")
//    public void deleteEventInCompilation(@PathVariable long compId, @PathVariable long eventId) {
//        log.info("Admin delete event id={} in compilation id={}", eventId, compId);
//        adminService.deleteEventInCompilation(compId, eventId);
//    }
//
//    @PatchMapping("/compilations/{compId}/events/{eventId}")
//    public void addEventInCompilation(@PathVariable long compId, @PathVariable long eventId) {
//        log.info("Admin add event id={} in compilation id={}", eventId, compId);
//        adminService.addEventInCompilation(compId, eventId);
//    }
}
