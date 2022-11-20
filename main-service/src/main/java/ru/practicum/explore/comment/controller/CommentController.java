package ru.practicum.explore.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.comment.dto.UpdateComment;
import ru.practicum.explore.comment.service.CommentService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/events/{eventId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long userId,
                                                @PathVariable Long eventId,
                                                @RequestBody CommentDto commentDto) {
        return commentService.createComment(userId, eventId, commentDto);
    }

    @DeleteMapping("/comment/{comId}")
    public ResponseEntity<Object> deleteComment(@PathVariable Long userId,
                                                @PathVariable Long comId) {
        return commentService.deleteComment(userId, comId);
    }

    @PatchMapping("/events/{eventId}/comment")
    public ResponseEntity<Object> updateComment(@PathVariable Long userId,
                                                @PathVariable Long eventId,
                                                @RequestBody UpdateComment updateComment) {
        return commentService.updateComment(userId, eventId, updateComment);
    }
}
