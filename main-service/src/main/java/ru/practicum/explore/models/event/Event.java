package ru.practicum.explore.models.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.models.category.Category;
import ru.practicum.explore.models.location.Location;
import ru.practicum.explore.models.user.User;
import ru.practicum.explore.statuses.event.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс события <b>id</b>,<b>annotation</b>,<b>category</b>,<b>description</b>,<b>createdOn</b>,<b>eventDate</b>,
 * <b>location</b>,<b>initiator</b>,<b>paid</b>,<b>participantLimit</b>,<b>publishedOn</b>,<b>requestModeration</b>,
 * <b>state</b>.
 *
 * @version 1.1
 * @autor Дмитрий Бармин
 */
@Entity
@Table(name = "events", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;
    private String description;
    @Column(name = "create_on")
    private LocalDateTime createdOn;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne
    private Location location;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "initiator_id")
    private User initiator;
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    private String title;
    @Enumerated(EnumType.STRING)
    private Status state;
}
