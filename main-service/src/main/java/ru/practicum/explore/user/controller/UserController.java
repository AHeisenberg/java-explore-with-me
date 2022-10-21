package ru.practicum.explore.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.dto.UpdateEventRequest;
import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.user.service.UserService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}/events")
    public Collection<EventShortDto> findAllEventsByUserId(@PathVariable Long userId,
                                                           @RequestParam(defaultValue = "0") Integer from,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Find all events by user id={}", userId);
        return userService.findAllEventsByUserId(userId, from, size);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto patchEventByUser(@PathVariable Long userId,
                                         @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("Patch event by user id={}", userId);
        return userService.patchEventByUser(userId, updateEventRequest);
    }

    @PostMapping("/{userId}/events")
    public EventFullDto postEvent(@PathVariable Long userId, @RequestBody NewEventDto newEventDto) {
        log.info("Post new event by id={}", userId);
        return userService.postEvent(userId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto findEventFull(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Get event id={} user id={}", eventId, userId);
        return userService.findEventFull(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelEventByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Cancel event id={} by user id={}", eventId, userId);
        return userService.cancelEventByUser(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public Collection<ParticipationRequestDto> findRequestByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Get request by user id={} and event id={}", userId, eventId);
        return userService.findRequestByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto approveConfirmUserByEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                                             @PathVariable Long reqId) {
        log.info("Approve request={} by user id={} and event id={}", reqId, userId, eventId);
        return userService.approveConfirmUserByEvent(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto approveRejectUserByEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                                            @PathVariable Long reqId) {
        log.info("Reject request={} by user id={} and event id={}", reqId, userId, eventId);
        return userService.approveRejectUserByEvent(userId, eventId, reqId);
    }

    @GetMapping("/{userId}/requests")
    public Collection<ParticipationRequestDto> findRequestsByUser(@PathVariable Long userId) {
        log.info("Find all request by id={}", userId);
        return userService.findRequestsByUser(userId);
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto postRequestByUser(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Post request by user id={} and event id={}", userId, eventId);
        return userService.postRequestUser(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByUser(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Patch request id={} by user id={}", userId, requestId);
        return userService.cancelRequestByUser(userId, requestId);
    }
}
