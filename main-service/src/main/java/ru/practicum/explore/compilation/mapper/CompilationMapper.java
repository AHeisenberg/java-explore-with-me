package ru.practicum.explore.compilation.mapper;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.model.Compilation;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {

    private final EventMapper eventMapper;

    private final ModelMapper mapper;

    public Compilation toCompilation(@NotNull NewCompilationDto newCompilationDto, List<Event> events) {
        return Compilation.builder()
                .id(newCompilationDto.getId())
                .events(events)
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public CompilationDto toCompilationDto(@NotNull Compilation compilation) {
        List<EventShortDto> eventShortDtos = compilation.getEvents()
                .stream()
                .map(event -> eventMapper.toEventShortDto(event))
                .collect(Collectors.toList());

        TypeMap<Compilation, CompilationDto> typeMap = mapper.getTypeMap(Compilation.class, CompilationDto.class);
        if (typeMap == null) {
            typeMap = mapper.createTypeMap(Compilation.class, CompilationDto.class);
            typeMap.addMappings(m -> m.skip(CompilationDto::setEvents));
        }
        CompilationDto compilationDto = mapper.map(compilation, CompilationDto.class);
        compilationDto.setEvents(eventShortDtos);
        return compilationDto;
    }
}
