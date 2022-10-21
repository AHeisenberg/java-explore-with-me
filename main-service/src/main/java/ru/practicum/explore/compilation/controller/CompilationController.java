package ru.practicum.explore.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.service.CompilationService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/compilations")
@Slf4j
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public Collection<CompilationDto> findAllCompilations(@RequestParam Boolean pinned,
                                                          @RequestParam(defaultValue = "0") int from,
                                                          @RequestParam(defaultValue = "10") int size) {
        log.info("Find all compilations");
        return compilationService.findAllCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public Optional<CompilationDto> findCompilationById(@PathVariable long compId) {
        log.info("Find compilation by id={}", compId);
        return compilationService.findCompilationById(compId);
    }
}
