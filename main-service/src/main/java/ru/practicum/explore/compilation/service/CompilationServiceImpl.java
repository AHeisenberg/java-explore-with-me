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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final CommonValidator commonValidator;
    private final EventRepository eventRepository;

    @Override
    public void pinCompilation(Long compId) {
        log.info("Admin pin compilation by id={}", compId);
        commonValidator.compilationValidator(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpinCompilation(Long compId) {
        log.info("Admin unpin compilation by id={}", compId);
        commonValidator.compilationValidator(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public Collection<CompilationDto> findAll(Boolean pinned, Integer from, Integer size) {
        log.info("Find all compilations");
        Pageable pageable = PageRequest.of(from / size, size);
        Collection<Compilation> compilationCollection =
                compilationRepository.findAllByPinnedOrderById(pinned, pageable);
        Collection<CompilationDto> compilationDtoCollection = new ArrayList<>();
        if (compilationCollection.isEmpty()) {
            return compilationDtoCollection;
        } else {
            for (Compilation c : compilationCollection) {
                List<EventShortDto> eventShortDtoList = new ArrayList<>();
                if (c.getEvents().size() != 0) {
                    eventShortDtoList = c.getEvents().stream()
                            .map(eventMapper::toEventShortDto)
                            .collect(Collectors.toList());
                }
                compilationDtoCollection.add(CompilationMapper.toCompilationDto(c, eventShortDtoList));
            }
        }
        return compilationDtoCollection;
    }

    @Override
    public Optional<CompilationDto> findCompilationById(Long compId) {
        log.info("Find compilation by id={}", compId);
        commonValidator.compilationValidator(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        if (compilation.getEvents().size() != 0) {
            eventShortDtoList = compilation.getEvents().stream()
                    .map(eventMapper::toEventShortDto)
                    .collect(Collectors.toList());
        }
        return Optional.of(CompilationMapper.toCompilationDto(compilation, eventShortDtoList));
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        log.info("Admin post compilation title={}", newCompilationDto.getTitle());
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto, events);
        compilationRepository.save(compilation);
        List<EventShortDto> eventShortDtoList = events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return CompilationMapper.toCompilationDto(compilation, eventShortDtoList);
    }

    @Override
    public void deleteCompilation(Long compId) {
        log.info("Admin delete compilation id={}", compId);
        commonValidator.compilationValidator(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEventInCompilation(Long compId, Long eventId) {
        log.info("Admin delete event id={} in compilation id={}", eventId, compId);
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
    public void addEventInCompilation(Long compId, Long eventId) {
        log.info("Admin add event id={} in compilation id={}", eventId, compId);
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
