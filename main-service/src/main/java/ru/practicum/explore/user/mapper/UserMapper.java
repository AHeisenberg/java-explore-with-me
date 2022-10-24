package ru.practicum.explore.user.mapper;

import com.sun.istack.NotNull;
import org.springframework.stereotype.Component;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.dto.UserDto.UserDtoBuilder;
import ru.practicum.explore.user.dto.UserShortDto;
import ru.practicum.explore.user.model.User;

@Component
public class UserMapper {
    public UserDto toUserDto(@NotNull User user) {
        UserDtoBuilder userDto = UserDto.builder();
        userDto.id(user.getId());
        userDto.name(user.getName());
        userDto.email(user.getEmail());
        return userDto.build();
    }

    public User toUser(@NotNull NewUserRequest newUserRequest) {
        return User.builder()
                .email(newUserRequest.getEmail())
                .name(newUserRequest.getName())
                .build();
    }

    public UserShortDto toUserShortDto(@NotNull User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
