package ru.practicum.explore.compilation.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.explore.compilation.dto.NewCompilationDto;

public interface CompilationService {

    ResponseEntity<Object> pinCompilation(Long compId);

    ResponseEntity<Object> unpinCompilation(Long compId);

    ResponseEntity<Object> findAll(Boolean pinned, Integer from, Integer size);

    ResponseEntity<Object> findCompilationById(Long compId);

    ResponseEntity<Object> createCompilation(NewCompilationDto newCompilationDto);

    ResponseEntity<Object> deleteCompilation(Long compId);

    ResponseEntity<Object> deleteEventInCompilation(Long compId, Long eventId);

    ResponseEntity<Object> addEventInCompilation(Long compId, Long eventId);
}
