package ru.practicum.explore.compilation.service;

import ru.practicum.explore.compilation.dto.CompilationDto;

import java.util.Collection;
import java.util.Optional;

/**
 * Интерфейс сервиса подборок событий
 */
public interface CompilationService {
    /*
    Получение подборок событий
    */
    Collection<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    /*
     Получение подборки событий по его id
    */
    Optional<CompilationDto> get(Long compId);
}
