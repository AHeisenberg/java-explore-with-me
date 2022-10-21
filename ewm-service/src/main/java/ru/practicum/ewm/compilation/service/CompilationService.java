package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;

import java.util.Collection;
import java.util.Optional;

public interface CompilationService {

    Collection<CompilationDto> findAllCompilations(Boolean pinned, int from, int size);

    Optional<CompilationDto> findCompilationById(long compId);

}
