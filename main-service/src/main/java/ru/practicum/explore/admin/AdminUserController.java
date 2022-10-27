package ru.practicum.explore.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Object> findAllUsers(@RequestParam List<Long> ids,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {
        return userService.findAllUsers(ids, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> postUser(@RequestBody NewUserRequest newUserRequest) {
        return userService.postUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
       return userService.deleteUser(userId);
    }
}
