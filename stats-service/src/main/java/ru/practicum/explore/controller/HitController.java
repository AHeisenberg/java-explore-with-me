package ru.practicum.explore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.EndpointHit;
import ru.practicum.explore.dto.ViewStats;
import ru.practicum.explore.service.HitService;

import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
public class HitController {
    private final HitService hitService;

    @Autowired
    public HitController(HitService hitService) {
        this.hitService = hitService;
    }

    @PostMapping("/hit")
    public EndpointHit createHit(@RequestBody EndpointHit endpointHit) {
        log.info("create hit {}", endpointHit);
        return hitService.createHit(endpointHit);
    }

    @GetMapping("/stats")
    public Collection<ViewStats> getStats(@RequestParam String start,
                                          @RequestParam String end,
                                          @RequestParam List<String> uris,
                                          @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("get stats {}", uris);
        return hitService.getStats(start, end, uris, unique);
    }
}
