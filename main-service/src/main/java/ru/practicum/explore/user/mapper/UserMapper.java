package ru.practicum.explore.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.dto.UserShortDto;
import ru.practicum.explore.user.model.User;

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
