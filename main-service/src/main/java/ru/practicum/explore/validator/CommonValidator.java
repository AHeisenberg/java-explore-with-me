package ru.practicum.explore.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore.category.repository.CategoryRepository;
import ru.practicum.explore.compilation.repository.CompilationRepository;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.exc.ObjectNotFoundException;
import ru.practicum.explore.request.repository.ParticipationRequestRepository;
import ru.practicum.explore.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class CommonValidator {
    private final CategoryRepository categoryRepository;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    private static final String OBJECT_NOT_FOUND = "Object not found id=%s";

    public void validateForUser(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new ObjectNotFoundException(String.format(OBJECT_NOT_FOUND, id));
        }
    }

    public void validateForEvent(Long id) {
        if (eventRepository.findById(id).isEmpty()) {
            throw new ObjectNotFoundException(String.format(OBJECT_NOT_FOUND, id));
        }
    }

    public void validateForCategory(Long id) {
        if (categoryRepository.findById(id).isEmpty()) {
            throw new ObjectNotFoundException(String.format(OBJECT_NOT_FOUND, id));
        }
    }

    public void validateForRequest(Long id) {
        if (participationRequestRepository.findById(id).isEmpty()) {
            throw new ObjectNotFoundException(String.format(OBJECT_NOT_FOUND, id));
        }
    }

    public void validateForCompilation(Long id) {
        if (compilationRepository.findById(id).isEmpty()) {
            throw new ObjectNotFoundException(String.format(OBJECT_NOT_FOUND, id));
        }
    }
}
