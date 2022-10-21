package ru.practicum.explore.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
@Slf4j
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getAllUsers(@RequestParam List<Long> ids,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.info("Admin get all users");
        return userService.findAllUsers(ids, from, size);
    }

    @PostMapping
    public UserDto postUser(@RequestBody NewUserRequest newUserRequest) {
        log.info("Admin post user");
        return userService.postUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Admin delete user by id={}", userId);
        userService.deleteUser(userId);
    }
}