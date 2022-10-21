package ru.practicum.explore.compilation.service;

import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;

import java.util.Collection;

public interface CompilationService {

    Collection<CompilationDto> findAllCompilations(Boolean pinned, int from, int size);

   CompilationDto findCompilationById(long compId);

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(long compId);

    void pinCompilation(long compId);

    void unpinCompilation(long compId);

    void addEventInCompilation(long compId, long eventId);

    void deleteEventInCompilation(long compId, long eventId);

}
