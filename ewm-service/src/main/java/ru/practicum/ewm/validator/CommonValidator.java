package ru.practicum.ewm.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exc.ObjectNotFoundException;
import ru.practicum.ewm.request.repository.ParticipationRequestRepository;
import ru.practicum.ewm.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class CommonValidator {
    private final CategoryRepository categoryRepository;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private static final String OBJECT_NOT_FOUND = "Object not found id=%s";

    public void userValidator(long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new ObjectNotFoundException(String.format(OBJECT_NOT_FOUND, id));
        }
    }

    public void eventValidator(long id) {
        if (eventRepository.findById(id).isEmpty()) {
            throw new ObjectNotFoundException(String.format(OBJECT_NOT_FOUND, id));
        }
    }

    public void categoryValidator(long id) {
        if (categoryRepository.findById(id).isEmpty()) {
            throw new ObjectNotFoundException(String.format(OBJECT_NOT_FOUND, id));
        }
    }

    public void requestValidator(long id) {
        if (participationRequestRepository.findById(id).isEmpty()) {
            throw new ObjectNotFoundException(String.format(OBJECT_NOT_FOUND, id));
        }
    }

    public void compilationValidator(long id) {
        if (compilationRepository.findById(id).isEmpty()) {
            throw new ObjectNotFoundException(String.format(OBJECT_NOT_FOUND, id));
        }
    }
}
