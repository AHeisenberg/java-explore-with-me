package ru.practicum.ewm.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.model.User;

@Component
public class UserMapper {

    public UserDto toUserDto(User user) {
        return user == null ? null :
                UserDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .build();
    }

    public User toUser(NewUserRequest newUserRequest) {
        return newUserRequest == null ? null :
                User.builder()
                        .email(newUserRequest.getEmail())
                        .name(newUserRequest.getName())
                        .build();
    }

    public UserShortDto toUserShortDto(User user) {
        return user == null ? null :
                UserShortDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build();
    }
}
