package ru.practicum.explore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс статистики <b>id</b>,<b>app</b>,<b>uri</b>,<b>ip</b>,<b>timestamp</b>.
 */
@Entity
@Table(name = "hits", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
