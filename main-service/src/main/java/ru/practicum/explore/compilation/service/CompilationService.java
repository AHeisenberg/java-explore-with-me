package ru.practicum.explore.compilation.service;

import ru.practicum.explore.compilation.dto.CompilationDto;

import java.util.Collection;
import java.util.Optional;

public interface CompilationService {

    Collection<CompilationDto> findAllCompilations(Boolean pinned, int from, int size);

    Optional<CompilationDto> findCompilationById(long compId);

}
