package ru.practicum.explore.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.service.CompilationService;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PatchMapping("/{compId}/pin")
    public ResponseEntity<Object> pinCompilation(@PathVariable Long compId) {
        return compilationService.pinCompilation(compId);
    }

    @DeleteMapping("/{compId}/pin")
    public ResponseEntity<Object> unpinCompilation(@PathVariable Long compId) {
        return   compilationService.unpinCompilation(compId);
    }

    @PostMapping
    public ResponseEntity<Object> createCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> deleteCompilation(@PathVariable Long compId) {
        return   compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public ResponseEntity<Object> deleteEventInCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        return   compilationService.deleteEventInCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public ResponseEntity<Object> addEventInCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        return compilationService.addEventInCompilation(compId, eventId);
    }
}
