package ru.practicum.explore.models.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Класс локация <b>id</b>,<b>lat</b>,<b>lon</b>.
 *
 * @version 1.1
 * @autor Дмитрий Бармин
 */
@Entity
@Table(name = "locations", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float lat;
    private Float lon;
}
