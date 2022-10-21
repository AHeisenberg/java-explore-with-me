package ru.practicum.explore.compilation.service;

import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;

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

    /*
Метод контроллера для добавления подборки событий админом
*/
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    /*
    Метод контроллера для удаления подборки событий админом
    */
    void deleteCompilation(Long compId);

    /*
    Метод контроллера для удаления из подборки события админом
    */
    void deleteEventInCompilation(Long compId, Long eventId);

    /*
    Метод контроллера для добавления в подборку события админом
    */
    void addEventInCompilation(Long compId, Long eventId);

    /*
     Метод контроллера закренления подборки событий админом
     */
    void unpinCompilation(Long compId);

    /*
    Метод контроллера для открепления подборки событий админом
    */
    void pinCompilation(Long compId);
}
