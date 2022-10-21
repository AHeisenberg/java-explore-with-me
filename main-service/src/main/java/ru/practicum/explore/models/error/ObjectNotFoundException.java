package ru.practicum.explore.models.error;

/**
 * Класс исключения ObjectNotFoundException
 *
 * @version 1.1
 * @autor Дмитрий Бармин
 */
public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
