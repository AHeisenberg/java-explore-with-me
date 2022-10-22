package ru.practicum.explore.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.category.repository.CategoryRepository;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.dto.UpdateEventRequest;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.EventStatus;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.exc.ForbiddenRequestException;
import ru.practicum.explore.exc.ObjectNotFoundException;
import ru.practicum.explore.location.model.Location;
import ru.practicum.explore.location.service.LocationService;
import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.request.mapper.RequestMapper;
import ru.practicum.explore.request.model.ParticipationRequest;
import ru.practicum.explore.request.model.RequestStatus;
import ru.practicum.explore.request.repository.ParticipationRequestRepository;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.mapper.UserMapper;
import ru.practicum.explore.user.model.User;
import ru.practicum.explore.user.repository.UserRepository;
import ru.practicum.explore.validator.CommonValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final ParticipationRequestRepository participationRequestRepository;
    private final CategoryRepository categoryRepository;
    private final CommonValidator commonValidator;

    private static final String EVENT_INITIATOR_IS_WRONG = "Event initiator is wrong";
    private static final String EVENT_STATUS_IS_WRONG = "Event status is wrong";
    private static final String CATEGORY_NOT_FOUND = "Category not found.";
    private static final String DATE_IS_INVALID = "Date is invalid.";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Collection<EventShortDto> findAllEventsByUserId(Long userId, Integer from, Integer size) {
        commonValidator.userValidator(userId);
        Pageable pageable = PageRequest.of(from / size, size);
        return eventRepository.findAllByInitiatorId(userId, pageable).stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto patchEventByUser(Long userId, UpdateEventRequest updateEventRequest) {
        commonValidator.userValidator(userId);
        commonValidator.eventValidator(updateEventRequest.getEventId());
        Event event = eventRepository.findById(updateEventRequest.getEventId()).get();
        validatorForPatchEventByUser(userId, event);
        if (updateEventRequest.getCategory() != null) {
            if (categoryRepository.findById(Long.valueOf(updateEventRequest.getCategory())).isEmpty()) {
                throw new ObjectNotFoundException(CATEGORY_NOT_FOUND);
            }
            Category category = categoryRepository.findById(Long.valueOf(updateEventRequest.getCategory())).get();
            event.setCategory(category);
        }
        eventMapper.updateEventFromNewEventDto(updateEventRequest, event);
        if (event.getState().equals(EventStatus.CANCELED)) {
            event.setState(EventStatus.PENDING);
        }
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto postEvent(Long userId, NewEventDto newEventDto) {
        commonValidator.userValidator(userId);
        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER);
        validatorForPostEvent(newEventDto, eventDate);
        User user = userRepository.findById(userId).get();
        Location location = locationService.save(newEventDto.getLocation());
        Category category = categoryRepository.findById(Long.valueOf(newEventDto.getCategory())).get();
        Event event = eventMapper.toEvent(newEventDto, user, location, category, eventDate);
        event.setState(EventStatus.PENDING);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto findEventFull(Long userId, Long eventId) {
        commonValidator.userValidator(userId);
        commonValidator.eventValidator(eventId);
        Event event = eventRepository.findById(eventId).get();
        validatorForSomeUser(userId, event);

        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto cancelEventByUser(Long userId, Long eventId) {
        commonValidator.userValidator(userId);
        commonValidator.eventValidator(eventId);
        Event event = eventRepository.findById(eventId).get();
        validatorVorFullEvent(userId, event);
        event.setState(EventStatus.CANCELED);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public Collection<ParticipationRequestDto> findRequestByUser(Long userId, Long eventId) {
        commonValidator.userValidator(userId);
        commonValidator.eventValidator(eventId);
        Event event = eventRepository.findById(eventId).get();
        validatorForSomeUser(userId, event);
        return participationRequestRepository.findAllByEvent(eventId, userId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto approveConfirmUserByEvent(Long userId, Long eventId, Long reqId) {
        commonValidator.userValidator(userId);
        commonValidator.eventValidator(eventId);
        commonValidator.requestValidator(reqId);
        Event event = eventRepository.findById(eventId).get();

        validatorForSomeUser(userId, event);

        ParticipationRequest participationRequest = participationRequestRepository.findById(reqId).get();
        Integer limitParticipant = participationRequestRepository.countByEvent_IdAndStatus(eventId,
                RequestStatus.CONFIRMED);
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(limitParticipant)) {
            participationRequest.setStatus(RequestStatus.REJECTED);
        }
        participationRequest.setStatus(RequestStatus.CONFIRMED);
        return requestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }

    @Override
    public ParticipationRequestDto approveRejectUserByEvent(Long userId, Long eventId, Long reqId) {
        commonValidator.userValidator(userId);
        commonValidator.eventValidator(eventId);
        commonValidator.requestValidator(reqId);
        Event event = eventRepository.findById(eventId).get();
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            return null;
        }
        ParticipationRequest participationRequest = participationRequestRepository.findById(reqId).get();
        participationRequest.setStatus(RequestStatus.REJECTED);
        return requestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }

    @Override
    public Collection<ParticipationRequestDto> findRequestsByUser(Long userId) {
        commonValidator.userValidator(userId);
        return participationRequestRepository.findAllByRequester_IdOrderById(userId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto postRequestUser(Long userId, Long eventId) {
        commonValidator.userValidator(userId);
        commonValidator.eventValidator(eventId);
        if (Objects.equals(eventRepository.findById(eventId).get().getInitiator().getId(), userId)) {
            throw new ForbiddenRequestException(EVENT_INITIATOR_IS_WRONG);
        }
        if (eventRepository.findById(eventId).get().getState().equals(EventStatus.PUBLISHED)) {
            User user = userRepository.findById(userId).get();
            Event event = eventRepository.findById(eventId).get();
            ParticipationRequest participationRequest = ParticipationRequest.builder()
                    .created(LocalDateTime.now())
                    .event(event)
                    .requester(user)
                    .status(RequestStatus.PENDING)
                    .build();
            Integer limitParticipant = participationRequestRepository.countByEvent_IdAndStatus(eventId,
                    RequestStatus.CONFIRMED);
            if (!event.getRequestModeration()) {
                participationRequest.setStatus(RequestStatus.CONFIRMED);
            }
            if (event.getParticipantLimit() != 0 && Objects.equals(event.getParticipantLimit(), limitParticipant)) {
                participationRequest.setStatus(RequestStatus.REJECTED);
            }
            return requestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
        } else {
            throw new ForbiddenRequestException(EVENT_INITIATOR_IS_WRONG);
        }
    }

    @Override
    public ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId) {
        commonValidator.userValidator(userId);
        commonValidator.requestValidator(requestId);
        if (!Objects.equals(participationRequestRepository.findById(requestId).get().getRequester().getId(), userId)) {
            throw new ForbiddenRequestException(EVENT_INITIATOR_IS_WRONG);
        }
        ParticipationRequest participationRequest = participationRequestRepository.findById(requestId).get();
        participationRequest.setStatus(RequestStatus.CANCELED);
        return requestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }

    @Override
    public Collection<UserDto> findAllUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return userRepository.findAllByIdOrderByIdDesc(ids, pageable).stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto postUser(NewUserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long userId) {
        commonValidator.userValidator(userId);
        userRepository.deleteById(userId);
    }

    private void validatorVorFullEvent(Long userId, Event event) {
        validatorForSomeUser(userId, event);
        if (event.getState().equals(EventStatus.PUBLISHED) || event.getState().equals(EventStatus.CANCELED)) {
            throw new ForbiddenRequestException(EVENT_STATUS_IS_WRONG);
        }
    }

    private void validatorForSomeUser(Long userId, Event event) {
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenRequestException(EVENT_INITIATOR_IS_WRONG);
        }
    }

    private void validatorForPostEvent(NewEventDto newEventDto, LocalDateTime eventDate) {
        if (!eventDate.isAfter(LocalDateTime.now().minusHours(2))) {
            throw new ForbiddenRequestException(DATE_IS_INVALID);
        }
        if (categoryRepository.findById(Long.valueOf(newEventDto.getCategory())).isEmpty()) {
            throw new ObjectNotFoundException(CATEGORY_NOT_FOUND);
        }
    }

    private void validatorForPatchEventByUser(Long userId, Event event) {
        validatorForSomeUser(userId, event);
        if (!event.getEventDate().isAfter(LocalDateTime.now().minusHours(2))) {
            throw new ForbiddenRequestException(DATE_IS_INVALID);
        }
        if (event.getState().equals(EventStatus.PUBLISHED)) {
            throw new ForbiddenRequestException(EVENT_STATUS_IS_WRONG);
        }
    }
}
