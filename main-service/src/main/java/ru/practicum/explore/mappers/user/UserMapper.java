package ru.practicum.explore.mappers.user;

import ru.practicum.explore.dto.user.NewUserRequest;
import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.dto.user.UserShortDto;
import ru.practicum.explore.models.user.User;

/**
 * Интерфейс маппера пользователя
 */
public interface UserMapper {
    /*
    Метод маппера  для получения dto пользователя из модели
    */
    UserDto toUserDto(User user);

    /*
    Метод маппера  для добовления модели пользователя из dto
    */
    User toUser(NewUserRequest newUserRequest);

    /*
    Метод маппера  для получения краткой информации о пользователе
    */
    UserShortDto toUserShortDto(User user);
}
