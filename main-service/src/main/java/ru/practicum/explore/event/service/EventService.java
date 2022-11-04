package ru.practicum.explore.event.service;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface EventService {

    ResponseEntity<Object> findAllEvents(Map<String, Object> parameters);

    ResponseEntity<Object> findEventById(Long id);

    void saveHitInStatsService(HttpServletRequest request);

}
