package ru.practicum.explore.controllers.publicApi.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.services.publicApi.event.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Публичный API для работы с событиями
 */
@RestController
@RequestMapping("/events")
@Slf4j
public class EventController {
    private EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /*
    Получение событий с возможностью фильтрации
    */
    @GetMapping
    public Collection<EventShortDto> getAll(@RequestParam String text,
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
        log.info("Find ail events by parameters {}", parameters);
        eventService.saveInStatService(request);
        return eventService.getAll(parameters);
    }

    /*
    Получение подробной информации об опубликованном событии по его идентификатору
    */
    @GetMapping("/{id}")
    public Optional<EventFullDto> getEvent(@PathVariable Long id, HttpServletRequest request) {
        eventService.saveInStatService(request);
        log.info("Get event by id={}", id);
        return eventService.getEvent(id);
    }
}
