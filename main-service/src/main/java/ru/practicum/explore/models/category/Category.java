package ru.practicum.explore.models.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Класс категории <b>id</b> и <b>name</b>.
 *
 * @version 1.1
 * @autor Дмитрий Бармин
 */
@Entity
@Table(name = "categories", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}
