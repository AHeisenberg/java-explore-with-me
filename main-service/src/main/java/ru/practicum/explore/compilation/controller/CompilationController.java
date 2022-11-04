package ru.practicum.explore.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.compilation.service.CompilationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestParam(required = false, defaultValue = "false") Boolean pinned,
                                          @RequestParam(required = false, defaultValue = "0") Integer from,
                                          @RequestParam(required = false, defaultValue = "10") Integer size) {
        return compilationService.findAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<Object> findCompilationById(@PathVariable Long compId) {
        return compilationService.findCompilationById(compId);
    }
}
