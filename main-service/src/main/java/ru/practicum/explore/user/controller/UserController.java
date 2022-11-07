package ru.practicum.explore.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.dto.UpdateEventRequest;
import ru.practicum.explore.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}/events")
    public ResponseEntity<Object> findAllEventsByUserId(@PathVariable Long userId,
                                                        @RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(defaultValue = "10") Integer size) {
        return userService.findAllEventsByUserId(userId, from, size);
    }

    @PatchMapping("/{userId}/events")
    public ResponseEntity<Object> patchEventByUser(@PathVariable Long userId,
                                                   @RequestBody UpdateEventRequest updateEventRequest) {
        return userService.patchEventByUser(userId, updateEventRequest);
    }



    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<Object> findEventFull(@PathVariable Long userId, @PathVariable Long eventId) {
        return userService.findEventFull(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<Object> cancelEventByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        return userService.cancelEventByUser(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<Object> findRequestByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        return userService.findRequestByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ResponseEntity<Object> approveConfirmUserByEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                                            @PathVariable Long reqId) {
        return userService.approveConfirmUserByEvent(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ResponseEntity<Object> approveRejectUserByEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                                           @PathVariable Long reqId) {
        return userService.approveRejectUserByEvent(userId, eventId, reqId);
    }

    @GetMapping("/{userId}/requests")
    public ResponseEntity<Object> findRequestsByUser(@PathVariable Long userId) {
        return userService.findRequestsByUser(userId);
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<Object> postRequestByUser(@PathVariable Long userId, @RequestParam Long eventId) {
        return userService.postRequestUser(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<Object> cancelRequestByUser(@PathVariable Long userId, @PathVariable Long requestId) {
        return userService.cancelRequestByUser(userId, requestId);
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<Object> postEvent(@PathVariable Long userId,
                                            @RequestBody NewEventDto newEventDto) {
        return userService.postEvent(userId, newEventDto);
    }
}
