package ru.practicum.explore.event.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.explore.event.dto.AdminUpdateEventRequest;

import java.util.Map;

public interface AdminEventService {

    ResponseEntity<Object> findAllEvents(Map<String, Object> parameters);

    ResponseEntity<Object> putEvent(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    ResponseEntity<Object> approvePublishEvent(Long eventId);

    ResponseEntity<Object> approveRejectEvent(Long eventId);

}
