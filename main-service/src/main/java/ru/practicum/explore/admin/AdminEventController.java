package ru.practicum.explore.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.AdminUpdateEventRequest;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.model.EventStatus;
import ru.practicum.explore.event.service.AdminEventService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class AdminEventController {

    private final AdminEventService adminEventService;

    @GetMapping
    public Collection<EventFullDto> findAllEvents(@RequestParam List<Long> users,
                                                  @RequestParam List<EventStatus> states,
                                                  @RequestParam List<Long> categories,
                                                  @RequestParam String rangeStart,
                                                  @RequestParam String rangeEnd,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size) {
        Map<String, Object> parameters = Map.of(
                "users", users,
                "states", states,
                "categories", categories,
                "rangeStart", rangeStart,
                "rangeEnd", rangeEnd,
                "from", from,
                "size", size
        );
        return adminEventService.findAllEvents(parameters);
    }

    @PutMapping("/{eventId}")
    public EventFullDto putEvent(@PathVariable Long eventId,
                                 @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        return adminEventService.putEvent(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto approvePublishEvent(@PathVariable Long eventId) {
        return adminEventService.approvePublishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto approveRejectEvent(@PathVariable Long eventId) {
        return adminEventService.approveRejectEvent(eventId);
    }

}
