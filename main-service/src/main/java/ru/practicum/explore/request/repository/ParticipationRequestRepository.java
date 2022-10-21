package ru.practicum.explore.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.request.model.ParticipationRequest;
import ru.practicum.explore.request.model.RequestStatus;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByRequester_IdOrderById(Long userId);

    @Query("select pr from ParticipationRequest  pr where pr.event.id=?1 and pr.event.initiator.id=?2")
    List<ParticipationRequest> findAllByEvent(Long eventId, Long userId);

    Integer countByEvent_IdAndStatus(Long eventId, RequestStatus confirmed);
}
