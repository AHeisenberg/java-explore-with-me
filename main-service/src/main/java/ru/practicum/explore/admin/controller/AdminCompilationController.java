package ru.practicum.explore.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.admin.service.AdminService;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminCompilationController {

    //    private final CompilationService compilationService;
    private final AdminService adminService;


    @PostMapping("/compilations")
    public CompilationDto createCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("Admin post compilation");
        return adminService.createCompilation(newCompilationDto);
    }

    //TODO
    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable long compId) {
        log.info("Admin delete compilation id={}", compId);
        adminService.deleteCompilation(compId);
//        compilationService.findCompilationById(compId);

    }

    @DeleteMapping("/compilations/{compId}/pin")
    public void unpinCompilation(@PathVariable long compId) {
        log.info("Admin unpin compilation id={}", compId);
        adminService.unpinCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public void pinCompilation(@PathVariable long compId) {
        log.info("Admin pin compilation id={}", compId);
        adminService.pinCompilation(compId);
    }


    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void deleteEventInCompilation(@PathVariable long compId, @PathVariable long eventId) {
        log.info("Admin delete event id={} in compilation id={}", eventId, compId);
        adminService.deleteEventInCompilation(compId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public void addEventInCompilation(@PathVariable long compId, @PathVariable long eventId) {
        log.info("Admin add event id={} in compilation id={}", eventId, compId);
        adminService.addEventInCompilation(compId, eventId);
    }
}
