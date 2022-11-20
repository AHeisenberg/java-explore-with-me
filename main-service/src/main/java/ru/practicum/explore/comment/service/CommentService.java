package ru.practicum.explore.comment.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.comment.dto.UpdateComment;


public interface CommentService {

    ResponseEntity<Object> createComment(long userId, long eventId, CommentDto commentDto);

    ResponseEntity<Object> deleteComment(long userId, long comId);

    ResponseEntity<Object> updateComment(long userId, long eventId, UpdateComment updateComment);
}
