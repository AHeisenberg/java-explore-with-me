package ru.practicum.explore.compilation.service;

import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;

import java.util.Collection;
import java.util.Optional;

public interface CompilationService {

    void pinCompilation(Long compId);

    void unpinCompilation(Long compId);

    Collection<CompilationDto> findAll(Boolean pinned, Integer from, Integer size);

    Optional<CompilationDto> findCompilationById(Long compId);

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    void deleteEventInCompilation(Long compId, Long eventId);

    void addEventInCompilation(Long compId, Long eventId);
}
