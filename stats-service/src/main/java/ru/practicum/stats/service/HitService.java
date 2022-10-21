package ru.practicum.stats.service;

import ru.practicum.stats.dto.EndpointHit;
import ru.practicum.stats.dto.ViewStats;

import java.util.Collection;
import java.util.List;

public interface HitService {

    EndpointHit createHit(EndpointHit endpointHit);

    Collection<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);
}
