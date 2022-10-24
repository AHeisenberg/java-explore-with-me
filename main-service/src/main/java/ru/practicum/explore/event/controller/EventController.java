package ru.practicum.explore.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @GetMapping
    public Collection<EventShortDto> findAllEvents(@RequestParam String text,
                                                   @RequestParam List<Long> categories,
                                                   @RequestParam Boolean paid,
                                                   @RequestParam String rangeStart,
                                                   @RequestParam String rangeEnd,
                                                   @RequestParam Boolean onlyAvailable,
                                                   @RequestParam String sort,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size,
                                                   HttpServletRequest request) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "categories", categories,
                "paid", paid,
                "rangeStart", rangeStart,
                "rangeEnd", rangeEnd,
                "onlyAvailable", onlyAvailable,
                "sort", sort,
                "from", from,
                "size", size
        );
        eventService.saveHitInStatsService(request);
        return eventService.findAllEvents(parameters);
    }

    @GetMapping("/{id}")
    public Optional<EventFullDto> findEventById(@PathVariable Long id, HttpServletRequest request) {
        eventService.saveHitInStatsService(request);
        return eventService.findEventById(id);
    }
}
