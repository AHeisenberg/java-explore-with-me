package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.validator.CommonValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final CommonValidator commonValidator;

    @Override
    public Collection<CompilationDto> findAllCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Collection<Compilation> compilationCollection =
                compilationRepository.findAllByPinned(pinned, pageable);
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
    public Optional<CompilationDto> findCompilationById(long compId) {
        commonValidator.compilationValidator(compId);
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
