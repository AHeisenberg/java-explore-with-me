package ru.practicum.explore.controllers.publicApi.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.services.publicApi.compilation.CompilationService;

import java.util.Collection;
import java.util.Optional;

/**
 * Публичный API для работы с подборками событий
 */
@RestController
@RequestMapping("/compilations")
@Slf4j
public class CompilationController {
    private final CompilationService compilationService;

    @Autowired
    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    /*
    Получение подборок событий по параметрам
    */
    @GetMapping
    public Collection<CompilationDto> getAll(@RequestParam Boolean pinned,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        log.info("find all Compilation");
        return compilationService.getAll(pinned, from, size);
    }

    /*
     Получение подборки событий по его id
    */
    @GetMapping("/{compId}")
    public Optional<CompilationDto> get(@PathVariable Long compId) {
        log.info("Get compilation id={}", compId);
        return compilationService.get(compId);
    }
}
