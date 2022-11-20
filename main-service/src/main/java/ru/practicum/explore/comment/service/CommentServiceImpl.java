package ru.practicum.explore.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.comment.dto.UpdateComment;
import ru.practicum.explore.comment.mapper.CommentMapper;
import ru.practicum.explore.comment.model.Comment;
import ru.practicum.explore.comment.repository.CommentRepository;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.EventStatus;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.exc.ForbiddenRequestException;
import ru.practicum.explore.exc.ObjectNotFoundException;
import ru.practicum.explore.user.model.User;
import ru.practicum.explore.user.repository.UserRepository;
import ru.practicum.explore.validator.CommonValidator;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommonValidator commonValidator;

    private static final String COMMENT_IS_INVALID = "Comment is invalid.";
    private static final String AUTHOR_OF_COMMENT_IS_WRONG = "Author of comment is wrong";
    private static final String EVENT_INITIATOR_IS_WRONG = "Event initiator is wrong";
    private static final String EVENT_STATUS_IS_WRONG = "Event status is wrong";

    @Override
    public ResponseEntity<Object> createComment(long userId, long eventId, CommentDto commentDto) {
        log.info("Add comment by user id={} and event id{}", userId, eventId);
        commonValidator.validateForUser(userId);
        commonValidator.validateForEvent(eventId);
        validateCreateComment(eventId, commentDto);
        User user = userRepository.findById(userId).get();
        Event event = eventRepository.findById(eventId).get();
        Comment comment = commentMapper.toComment(commentDto, user, event);
        return ResponseEntity.ok(commentMapper.toCommentDto(commentRepository.save(comment)));
    }

    @Override
    public ResponseEntity<Object> deleteComment(long userId, long comId) {
        log.info("Delete comment by user id={} and comment id{}", userId, comId);
        commonValidator.validateForUser(userId);
        validateDeleteComment(userId, comId);
        commentRepository.deleteById(comId);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<Object> updateComment(long userId, long eventId, UpdateComment updateComment) {
        log.info("Update comment by user id={} and event id{}", userId, eventId);
        commonValidator.validateForUser(userId);
        commonValidator.validateForEvent(eventId);
        validateUpdateComment(userId, eventId, updateComment);
        Comment comment = commentRepository.findById(updateComment.getId()).get();
        commentMapper.updateComment(updateComment, comment);
        return ResponseEntity.ok(commentMapper.toCommentDto(commentRepository.save(comment)));
    }

    private void validateCreateComment(long eventId, CommentDto commentDto) {
        if (commentDto.getText().isEmpty() || commentDto.getText().isBlank()) {
            throw new ForbiddenRequestException(COMMENT_IS_INVALID);
        }
        if (!eventRepository.findById(eventId).get().getState().equals(EventStatus.PUBLISHED)) {
            throw new ForbiddenRequestException(EVENT_INITIATOR_IS_WRONG);
        }
    }

    private void validateDeleteComment(long userId, long comId) {
        if (commentRepository.findById(comId).isEmpty()) {
            throw new ObjectNotFoundException(COMMENT_IS_INVALID);
        }
        if (!Objects.equals(commentRepository.findById(comId).get().getAuthor().getId(), userId)) {
            throw new ForbiddenRequestException(AUTHOR_OF_COMMENT_IS_WRONG);
        }
    }

    private void validateUpdateComment(long userId, long eventId, UpdateComment updateComment) {
        if (updateComment.getId() == null) {
            throw new ForbiddenRequestException(COMMENT_IS_INVALID);
        }
        if (commentRepository.findById(updateComment.getId()).isEmpty()) {
            throw new ObjectNotFoundException(COMMENT_IS_INVALID);
        }
        if (updateComment.getText().length() == 0 || updateComment.getText() == null) {
            throw new ForbiddenRequestException(COMMENT_IS_INVALID);
        }
        if (!eventRepository.findById(eventId).get().getState().equals(EventStatus.PUBLISHED)) {
            throw new ForbiddenRequestException(EVENT_STATUS_IS_WRONG);
        }
        if (!Objects.equals(commentRepository.findById(updateComment.getId()).get().getAuthor().getId(), userId)) {
            throw new ForbiddenRequestException(AUTHOR_OF_COMMENT_IS_WRONG);
        }
    }
}
