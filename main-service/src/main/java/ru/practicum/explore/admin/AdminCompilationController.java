package ru.practicum.explore.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;

@RestController
@RequestMapping(path = "/admin/compilations")
@Slf4j
@RequiredArgsConstructor
public class AdminCompilationController {
    private final AdminService adminService;

    @PostMapping
    public CompilationDto createCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("Admin post compilation");
        return adminService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Admin delete comtilation id={}", compId);
        adminService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventInCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Admin delete event id={} in compilation id={}", eventId, compId);
        adminService.deleteEventInCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventInCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Admin add event id={} in compilation id={}", eventId, compId);
        adminService.addEventInCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        log.info("Admin unpin compilation id={}", compId);
        adminService.unpinCompilation(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.info("Admin pin compilation id={}", compId);
        adminService.pinCompilation(compId);
    }
}
