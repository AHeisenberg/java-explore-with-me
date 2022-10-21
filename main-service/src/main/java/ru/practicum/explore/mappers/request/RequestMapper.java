package ru.practicum.explore.mappers.request;

import ru.practicum.explore.dto.request.ParticipationRequestDto;
import ru.practicum.explore.models.request.ParticipationRequest;

/**
 * Интерфейс маппера Запросов на участие в событие
 */
public interface RequestMapper {
    /*
    Метод маппера для получения dto запроса из модели запроса на участие в событие
    */
    ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest);
}
