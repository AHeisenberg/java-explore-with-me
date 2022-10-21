package ru.practicum.explore.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.mapper.CompilationMapper;
import ru.practicum.explore.compilation.model.Compilation;
import ru.practicum.explore.compilation.repository.CompilationRepository;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.validator.CommonValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final CommonValidator commonValidator;

    @Override
    public Collection<CompilationDto> findAllCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        boolean compilationsExist = !compilationRepository.findAll().isEmpty();
        Collection<Compilation> compilationCollection = compilationRepository.findAllByPinned(pinned, pageable);
        Collection<CompilationDto> compilationDtoCollection = new ArrayList<>();

        for (Compilation c : compilationCollection) {
            List<EventShortDto> eventShortDtoList = new ArrayList<>();
            if (c.getEvents().size() != 0) {
                eventShortDtoList = c.getEvents().stream()
                        .map(eventMapper::toEventShortDto)
                        .collect(Collectors.toList());
            }
            compilationDtoCollection.add(compilationMapper.toCompilationDto(c, eventShortDtoList));
        }

        if (compilationCollection.isEmpty()) {
            log.info("Compilations not exist");
            return compilationDtoCollection;
        } else {

        }
       log.info("Find all compilations");
        return compilationDtoCollection;
    }

    @Override
    public CompilationDto findCompilationById(long compId) {
        commonValidator.compilationValidator(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        if (compilation.getEvents().size() != 0) {
            eventShortDtoList = compilation.getEvents().stream()
                    .map(eventMapper::toEventShortDto)
                    .collect(Collectors.toList());
        }
        return compilationMapper.toCompilationDto(compilation, eventShortDtoList);
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto, events);
        compilationRepository.save(compilation);
        List<EventShortDto> eventShortDtoList = events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return compilationMapper.toCompilationDto(compilation, eventShortDtoList);
    }

    @Override
    public void deleteCompilation(long compId) {
        commonValidator.compilationValidator(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public void pinCompilation(long compId) {
        commonValidator.compilationValidator(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpinCompilation(long compId) {
        commonValidator.compilationValidator(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void deleteEventInCompilation(long compId, long eventId) {
        commonValidator.compilationValidator(compId);
        commonValidator.eventValidator(eventId);
        Compilation compilation = compilationRepository.findById(compId).get();
        Event event = eventRepository.findById(eventId).get();
        if (!compilation.getEvents().contains(event)) {
            return;
        }
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void addEventInCompilation(long compId, long eventId) {
        commonValidator.compilationValidator(compId);
        commonValidator.eventValidator(eventId);
        Compilation compilation = compilationRepository.findById(compId).get();
        Event event = eventRepository.findById(eventId).get();
        if (compilation.getEvents().contains(event)) {
            return;
        }
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
    }
}
