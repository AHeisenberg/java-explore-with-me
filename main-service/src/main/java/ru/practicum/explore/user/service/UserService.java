package ru.practicum.explore.user.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.dto.UpdateEventRequest;
import ru.practicum.explore.user.dto.NewUserRequest;

import java.util.List;

public interface UserService {

    ResponseEntity<Object> findAllEventsByUserId(Long userId, Integer from, Integer size);

    ResponseEntity<Object> patchEventByUser(Long userId, UpdateEventRequest updateEventRequest);

    ResponseEntity<Object> postEvent(Long userId, NewEventDto newEventDto);

    ResponseEntity<Object> findEventFull(Long userId, Long eventId);

    ResponseEntity<Object> cancelEventByUser(Long userId, Long eventId);

    ResponseEntity<Object> findRequestByUser(Long userId, Long eventId);

    ResponseEntity<Object> approveConfirmUserByEvent(Long userId, Long eventId, Long reqId);

    ResponseEntity<Object> approveRejectUserByEvent(Long userId, Long eventId, Long reqId);

    ResponseEntity<Object> findRequestsByUser(Long userId);

    ResponseEntity<Object> postRequestUser(Long userId, Long eventId);

    ResponseEntity<Object> cancelRequestByUser(Long userId, Long requestId);

    ResponseEntity<Object> findAllUsers(List<Long> ids, Integer from, Integer size);

    ResponseEntity<Object> postUser(NewUserRequest newUserRequest);

    ResponseEntity<Object> deleteUser(Long userId);

}
