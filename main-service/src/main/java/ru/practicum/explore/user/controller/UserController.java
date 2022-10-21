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
    public Collection<EventShortDto> findAllEventsByUserId(@PathVariable long userId,
                                                           @RequestParam(defaultValue = "0", required = false) int from,
                                                           @RequestParam(defaultValue = "10", required = false) int size) {
        log.info("Find all events by user id={}", userId);
        return userService.findAllEventsByUserId(userId, from, size);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto patchEventByUser(@PathVariable long userId,
                                         @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("Patch event by user id={}", userId);
        return userService.patchEventByUser(userId, updateEventRequest);
    }

    @PostMapping("/{userId}/events")
    public EventFullDto postEvent(@PathVariable long userId, @RequestBody NewEventDto newEventDto) {
        log.info("Post new event by id={}", userId);
        return userService.postEvent(userId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventFull(@PathVariable long userId, @PathVariable long eventId) {
        log.info("Get event id={} user id={}", eventId, userId);
        return userService.getEventFull(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable long userId, @PathVariable long eventId) {
        log.info("Cancel event id={} by user id={}", eventId, userId);
        return userService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public Collection<ParticipationRequestDto> getRequestByUser(@PathVariable long userId, @PathVariable long eventId) {
        log.info("Get request by user id={} and event id={}", userId, eventId);
        return userService.getRequestByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto approveConfirmUserByEvent(@PathVariable long userId, @PathVariable long eventId,
                                                             @PathVariable long reqId) {
        log.info("Approve request={} by user id={} and event id={}", reqId, userId, eventId);
        return userService.approveConfirmUserByEvent(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto approveRejectUserByEvent(@PathVariable long userId, @PathVariable long eventId,
                                                            @PathVariable long reqId) {
        log.info("Reject request={} by user id={} and event id={}", reqId, userId, eventId);
        return userService.approveRejectUserByEvent(userId, eventId, reqId);
    }

    @GetMapping("/{userId}/requests")
    public Collection<ParticipationRequestDto> getRequestsByUser(@PathVariable long userId) {
        log.info("Find all request by id={}", userId);
        return userService.getRequestsByUser(userId);
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
