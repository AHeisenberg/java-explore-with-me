package ru.practicum.explore.compilation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.model.Compilation;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.model.Event;

import java.util.List;

@Component
public class CompilationMapper {
    public Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return newCompilationDto == null ? null :
                Compilation.builder()
                        .id(newCompilationDto.getId())
                        .events(events)
                        .pinned(newCompilationDto.getPinned())
                        .title(newCompilationDto.getTitle())
                        .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> list) {
        return compilation == null ? null : CompilationDto.builder()
                .id(compilation.getId())
                .events(list)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}
