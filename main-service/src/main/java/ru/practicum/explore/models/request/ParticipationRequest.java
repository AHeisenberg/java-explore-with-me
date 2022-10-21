package ru.practicum.explore.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.models.event.Event;
import ru.practicum.explore.models.user.User;
import ru.practicum.explore.statuses.request.StatusRequest;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс заявки на участия <b>id</b>,<b>created</b>,<b>event</b>,<b>requester</b>,<b>status</b>.
 *
 * @version 1.1
 * @autor Дмитрий Бармин
 */
@Entity
@Table(name = "requests", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @Enumerated(EnumType.STRING)
    private StatusRequest status;
}
