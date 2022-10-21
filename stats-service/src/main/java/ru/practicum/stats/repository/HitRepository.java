package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Integer> {

    @Query("select h.uri from Hit h where h.timestamp>=?1 AND h.timestamp<=?2")
    List<String> findAllByTime(LocalDateTime startTime, LocalDateTime endTime);

    @Query("select h from Hit h where h.uri=?1 AND h.timestamp>=?2 AND h.timestamp<=?3")
    List<Hit> findAllByUri(String uri, LocalDateTime startTime, LocalDateTime endTime);
}
