package ru.practicum.explore.admin;

import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;

/**
 * Интерфейс сервиса пользователей
 */
public interface AdminService {


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
