package ru.practicum.explore.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.service.CompilationService;

@RestController
@RequestMapping(path = "/admin/compilations")
@Slf4j
@RequiredArgsConstructor
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.info("Admin pin compilation id={}", compId);
        compilationService.pinCompilation(compId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        log.info("Admin unpin compilation id={}", compId);
        compilationService.unpinCompilation(compId);
    }

    @PostMapping
    public CompilationDto createCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("Admin post compilation title={}", newCompilationDto.getTitle());
        return compilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Admin delete compilation id={}", compId);
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventInCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Admin delete event id={} in compilation id={}", eventId, compId);
        compilationService.deleteEventInCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventInCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Admin add event id={} in compilation id={}", eventId, compId);
        compilationService.addEventInCompilation(compId, eventId);
    }
}
