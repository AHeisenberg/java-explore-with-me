package ru.practicum.explore.mappers.request;

import org.springframework.stereotype.Component;
import ru.practicum.explore.dto.request.ParticipationRequestDto;
import ru.practicum.explore.models.request.ParticipationRequest;

import java.time.format.DateTimeFormatter;

@Component
public class RequestMapperImpl implements RequestMapper {
    @Override
    public ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .created(participationRequest.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .event(participationRequest.getEvent().getId())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getStatus().toString())
                .build();
    }
}
