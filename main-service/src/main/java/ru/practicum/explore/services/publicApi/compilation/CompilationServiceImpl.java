package ru.practicum.explore.services.publicApi.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.errors.validate.ObjectValidate;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.mappers.compilation.CompilationMapper;
import ru.practicum.explore.models.compilation.Compilation;
import ru.practicum.explore.repositories.compilation.CompilationRepository;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.mappers.event.EventMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompilationServiceImpl implements CompilationService {
    private CompilationRepository compilationRepository;
    private CompilationMapper compilationMapper;
    private EventMapper eventMapper;
    private ObjectValidate objectValidate;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, CompilationMapper compilationMapper,
                                  EventMapper eventMapper, ObjectValidate objectValidate) {
        this.compilationRepository = compilationRepository;
        this.compilationMapper = compilationMapper;
        this.eventMapper = eventMapper;
        this.objectValidate = objectValidate;
    }

    @Override
    public Collection<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Collection<Compilation> compilationCollection =
                compilationRepository.findAllByPinnedOrderById(pinned, pageable);
        Collection<CompilationDto> compilationDtoCollection = new ArrayList<>();
        if (!compilationCollection.isEmpty()) {
            for (Compilation c : compilationCollection) {
                List<EventShortDto> eventShortDtoList = new ArrayList<>();
                if (c.getEvents().size() != 0) {
                    eventShortDtoList = c.getEvents().stream()
                            .map(eventMapper::toEventShortDto)
                            .collect(Collectors.toList());
                }
                compilationDtoCollection.add(compilationMapper.toCompilationDto(c, eventShortDtoList));
            }
        }
        return compilationDtoCollection;
    }

    @Override
    public Optional<CompilationDto> get(Long compId) {
        objectValidate.validateCompilation(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        if (compilation.getEvents().size() != 0) {
            eventShortDtoList = compilation.getEvents().stream()
                    .map(eventMapper::toEventShortDto)
                    .collect(Collectors.toList());
        }
        return Optional.of(compilationMapper.toCompilationDto(compilation, eventShortDtoList));
    }
}
