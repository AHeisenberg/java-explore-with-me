package ru.practicum.explore.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.service.CompilationService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
@Slf4j
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public Collection<CompilationDto> findAll(@RequestParam Boolean pinned,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        log.info("Find all compilations");
        return compilationService.findAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public Optional<CompilationDto> findCompilationById(@PathVariable Long compId) {
        log.info("Find compilation by id={}", compId);
        return compilationService.findCompilationById(compId);
    }
}
