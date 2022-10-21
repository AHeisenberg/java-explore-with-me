package ru.practicum.explore.mappers.compilation;

import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.NewCompilationDto;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.models.compilation.Compilation;
import ru.practicum.explore.models.event.Event;

import java.util.List;

/**
 * Интерфейс маппера подбоки событий
 */
public interface CompilationMapper {
    /*
    Метод маппера из dto новой подборки в модель подборки
    */
    Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events);

    /*
    Метод маппера из модели подборки событий в dto подборки
    */
    CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> list);
}
