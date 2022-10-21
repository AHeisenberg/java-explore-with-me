package ru.practicum.explore.errors.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.explore.models.error.ObjectNotFoundException;
import ru.practicum.explore.repositories.category.CategoryRepository;
import ru.practicum.explore.repositories.compilation.CompilationRepository;
import ru.practicum.explore.repositories.event.EventRepository;
import ru.practicum.explore.repositories.request.ParticipationRequestRepository;
import ru.practicum.explore.repositories.user.UserRepository;

/**
 * Класс валидации
 */
@Component
public class ObjectValidate {
    private CategoryRepository categoryRepository;
    private CompilationRepository compilationRepository;
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private ParticipationRequestRepository participationRequestRepository;

    @Autowired
    public ObjectValidate(CategoryRepository categoryRepository, CompilationRepository compilationRepository,
                          EventRepository eventRepository, UserRepository userRepository,
                          ParticipationRequestRepository participationRequestRepository) {
        this.categoryRepository = categoryRepository;
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.participationRequestRepository = participationRequestRepository;
    }

    public void validateUser(Long userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new ObjectNotFoundException(String.format("User not found id=%s", userId));
        }
    }

    public void validateEvent(Long eventId) {
        if (!eventRepository.findById(eventId).isPresent()) {
            throw new ObjectNotFoundException(String.format("Event not found id=%s", eventId));
        }
    }

    public void validateCategory(Long catId) {
        if (!categoryRepository.findById(catId).isPresent()) {
            throw new ObjectNotFoundException(String.format("Category not found id=%s", catId));
        }
    }

    public void validateRequest(Long reqId) {
        if (!participationRequestRepository.findById(reqId).isPresent()) {
            throw new ObjectNotFoundException(String.format("Request not found id=%s", reqId));
        }
    }

    public void validateCompilation(Long compId) {
        if (!compilationRepository.findById(compId).isPresent()) {
            throw new ObjectNotFoundException(String.format("Compilation not found id=%s", compId));
        }
    }
}
