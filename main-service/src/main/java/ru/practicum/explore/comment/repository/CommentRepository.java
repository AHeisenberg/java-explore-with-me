package ru.practicum.explore.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.comment.model.Comment;
import ru.practicum.explore.event.model.Event;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEventOrderByCreated(Event event);
}

