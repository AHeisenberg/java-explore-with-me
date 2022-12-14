package ru.practicum.explore.request.mapper;

import com.sun.istack.NotNull;
import org.springframework.stereotype.Component;
import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.request.model.ParticipationRequest;

import java.time.format.DateTimeFormatter;

@Component
public class RequestMapper {
    public ParticipationRequestDto toParticipationRequestDto(@NotNull ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .created(participationRequest.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .event(participationRequest.getEvent().getId())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getStatus().toString())
                .build();
    }
}
