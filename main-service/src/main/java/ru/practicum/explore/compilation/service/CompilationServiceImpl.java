package ru.practicum.explore.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.mapper.CompilationMapper;
import ru.practicum.explore.compilation.model.Compilation;
import ru.practicum.explore.compilation.repository.CompilationRepository;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.validator.CommonValidator;

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
    private final CommonValidator commonValidator;
    private final EventRepository eventRepository;

    @Override
    public ResponseEntity<Object> pinCompilation(Long compId) {
        log.info("Admin pin compilation by id={}", compId);
        commonValidator.validateForCompilation(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.setPinned(true);
        return ResponseEntity.ok(compilationRepository.save(compilation));
    }

    @Override
    public ResponseEntity<Object> unpinCompilation(Long compId) {
        log.info("Admin unpin compilation by id={}", compId);
        commonValidator.validateForCompilation(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.setPinned(false);
        return ResponseEntity.ok(compilationRepository.save(compilation));
    }

    @Override
    public ResponseEntity<Object> findAll(Boolean pinned, Integer from, Integer size) {
        log.info("Find all categories from={}, size={}", from, size);
        PageRequest page = PageRequest.of(from / size, size);
        Collection<Compilation> compilations = compilationRepository.findAll(pinned, page);
        List<CompilationDto> compilationsDto =
                compilations.stream().map(compilationMapper::toCompilationDto).collect(Collectors.toList());
        return ResponseEntity.ok(compilationsDto);
    }

    @Override
    public ResponseEntity<Object> findCompilationById(Long compId) {
        log.info("Find compilation by id={}", compId);
        commonValidator.validateForCompilation(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        return ResponseEntity.ok(Optional.of(compilationMapper.toCompilationDto(compilation)));
    }

    @Override
    public ResponseEntity<Object> createCompilation(NewCompilationDto newCompilationDto) {
        log.info("Admin post compilation title={}", newCompilationDto.getTitle());
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto, events);
        compilationRepository.save(compilation);
        return ResponseEntity.ok(compilationMapper.toCompilationDto(compilation));
    }

    @Override
    public ResponseEntity<Object> deleteCompilation(Long compId) {
        log.info("Admin delete compilation id={}", compId);
        commonValidator.validateForCompilation(compId);
        compilationRepository.deleteById(compId);
        log.info("Admin has compilation id={} deleted", compId);
        log.info("Compilation  with id={} is not exist={}", compId, !compilationRepository.existsById(compId));
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<Object> deleteEventInCompilation(Long compId, Long eventId) {
        log.info("Admin delete event id={} in compilation id={}", eventId, compId);
        commonValidator.validateForCompilation(compId);
        commonValidator.validateForEvent(eventId);
        Compilation compilation = compilationRepository.findById(compId).get();
        Event event = eventRepository.findById(eventId).get();
        if (!compilation.getEvents().contains(event)) {
            return ResponseEntity.ok(null);
        }
        compilation.getEvents().remove(event);
        return ResponseEntity.ok(compilationRepository.save(compilation));
    }

    @Override
    public ResponseEntity<Object> addEventInCompilation(Long compId, Long eventId) {
        log.info("Admin add event id={} in compilation id={}", eventId, compId);
        commonValidator.validateForCompilation(compId);
        commonValidator.validateForEvent(eventId);
        Compilation compilation = compilationRepository.findById(compId).get();
        Event event = eventRepository.findById(eventId).get();
        compilation.getEvents().add(event);
        Compilation savedCompilation = compilationRepository.save(compilation);
        return ResponseEntity.ok(compilationMapper.toCompilationDto(savedCompilation));
    }
}
