package ru.practicum.explore.controllers.privateApi.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.dto.event.NewEventDto;
import ru.practicum.explore.dto.event.UpdateEventRequest;
import ru.practicum.explore.dto.request.ParticipationRequestDto;
import ru.practicum.explore.services.privateApi.user.UserService;

import java.util.Collection;

/**
 * Закрытый API для работы с событиями и для работы с запросами текущего пользователя на участие в событиях
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
    Метод контроллера для получение событий, добавленных текущим пользователем.
    */
    @GetMapping("/{userId}/events")
    public Collection<EventShortDto> findAllEventsByUserId(@PathVariable Long userId,
                                                           @RequestParam(defaultValue = "0") Integer from,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("User find all events by id={}", userId);
        return userService.findAllEventsByUserId(userId, from, size);
    }

    /*
    Метод контроллера для изменение события добавленного текущим пользователем.
    */
    @PatchMapping("/{userId}/events")
    public EventFullDto patchEventByUser(@PathVariable Long userId,
                                         @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("User update event by id={}", userId);
        return userService.patchEventByUser(userId, updateEventRequest);
    }

    /*
    Метод контроллера для добавление нового события.
    */
    @PostMapping("/{userId}/events")
    public EventFullDto postEvent(@PathVariable Long userId, @RequestBody NewEventDto newEventDto) {
        log.info("User Post new event by id={}", userId);
        return userService.postEvent(userId, newEventDto);
    }

    /*
    Получение полной информации о событии добавленном текущим пользователем.
    */
    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventFull(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("User Get event id={} userId={}", eventId, userId);
        return userService.getEventFull(userId, eventId);
    }

    /*
    Отмена события добавленного текущим пользователем.
    */
    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("User cancel eventId={} by userId={}", eventId, userId);
        return userService.cancelEvent(userId, eventId);
    }

    /*
    Получение информации о запросах на участие в событии текущего пользователя.
    */
    @GetMapping("/{userId}/events/{eventId}/requests")
    public Collection<ParticipationRequestDto> getRequestByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("User Get request by userIs={} and eventId={}", userId, eventId);
        return userService.getRequestByUser(userId, eventId);
    }

    /*
    Подтверждение чужой заявки на участие в событии текущего пользователя
    */
    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto approveConfirmUserByEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                                             @PathVariable Long reqId) {
        log.info("User approve request={} by userIs={} and eventId={}", reqId, userId, eventId);
        return userService.approveConfirmUserByEvent(userId, eventId, reqId);
    }

    /*
    Отклонение чужой заявки на участие в событии текущего пользователя.
    */
    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto approveRejectUserByEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                                            @PathVariable Long reqId) {
        log.info("User reject request={} by userIs={} and eventId={}", reqId, userId, eventId);
        return userService.approveRejectUserByEvent(userId, eventId, reqId);
    }

    /*
    Получение информации о заявках текущего пользователя на участие в чужих событиях.
    */
    @GetMapping("/{userId}/requests")
    public Collection<ParticipationRequestDto> getRequestsByUser(@PathVariable Long userId) {
        log.info("User find all request byid={}", userId);
        return userService.getRequestsByUser(userId);
    }

    /*
    Добавление запроса от текущего пользователя на участие в событии.
    */
    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto postRequestUser(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("User post request by userIs={} and eventId={}", userId, eventId);
        return userService.postRequestUser(userId, eventId);
    }

    /*
    Отмена своего запроса на участие в событии.
    */
    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByUser(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("User patch requestId={} by eserId={}", userId, requestId);
        return userService.cancelRequestByUser(userId, requestId);
    }
}
