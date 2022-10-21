package ru.practicum.explore.exc;

/**
 * Класс исключения ForbiddenRequestException
 *
 * @version 1.1
 * @autor Дмитрий Бармин
 */
public class ForbiddenRequestException extends RuntimeException {
    public ForbiddenRequestException(String message) {
        super(message);
    }
}
