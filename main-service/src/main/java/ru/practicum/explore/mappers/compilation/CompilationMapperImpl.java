package ru.practicum.explore.mappers.compilation;

import org.springframework.stereotype.Component;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.NewCompilationDto;
import ru.practicum.explore.models.compilation.Compilation;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.models.event.Event;

import java.util.List;

@Component
public class CompilationMapperImpl implements CompilationMapper {
    @Override
    public Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        if (newCompilationDto == null) {
            return null;
        }
        return Compilation.builder()
                .id(newCompilationDto.getId())
                .events(events)
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    @Override
    public CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> list) {
        if (compilation == null) {
            return null;
        }
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(list)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}
