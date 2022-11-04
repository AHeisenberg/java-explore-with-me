package ru.practicum.explore.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.category.repository.CategoryRepository;
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
import ru.practicum.explore.request.mapper.RequestMapper;
import ru.practicum.explore.request.model.ParticipationRequest;
import ru.practicum.explore.request.model.RequestStatus;
import ru.practicum.explore.request.repository.ParticipationRequestRepository;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.mapper.UserMapper;
import ru.practicum.explore.user.model.User;
import ru.practicum.explore.user.repository.UserRepository;
import ru.practicum.explore.validator.CommonValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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
    public ResponseEntity<Object> findAllEventsByUserId(Long userId, Integer from, Integer size) {
        log.info("Find all events by user id={}, from={}, size={}", userId, from, size);
        commonValidator.validateForUser(userId);
        Pageable pageable = PageRequest.of(from / size, size);
        return ResponseEntity.ok(eventRepository.findAllByInitiatorId(userId, pageable).stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<Object> patchEventByUser(Long userId, UpdateEventRequest updateEventRequest) {
        log.info("Patch event by user id={}", userId);
        commonValidator.validateForUser(userId);
        commonValidator.validateForEvent(updateEventRequest.getEventId());
        Event event = eventRepository.findById(updateEventRequest.getEventId()).get();
        validateForPatchEventByUser(userId, event);
        if (updateEventRequest.getCategory() != null) {
            if (categoryRepository.findById(Long.valueOf(updateEventRequest.getCategory())).isEmpty()) {
                throw new ObjectNotFoundException(CATEGORY_NOT_FOUND);
            }
            Category category = categoryRepository.findById(Long.valueOf(updateEventRequest.getCategory())).get();
            event.setCategory(category);
        }
        updateEventFromNewEventDto(updateEventRequest, event);
        if (event.getState().equals(EventStatus.CANCELED)) {
            event.setState(EventStatus.PENDING);
        }
        return ResponseEntity.ok(eventMapper.toEventFullDto(eventRepository.save(event)));
    }


    @Override
    public ResponseEntity<Object> postEvent(Long userId, NewEventDto newEventDto) {
        log.info("Post new event by id={}", userId);
        commonValidator.validateForUser(userId);
        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER);
        validateForPostEvent(newEventDto, eventDate);
        User user = userRepository.findById(userId).get();
        Location location = locationService.save(newEventDto.getLocation());
        Category category = categoryRepository.findById(Long.valueOf(newEventDto.getCategory())).get();
        Event event = eventMapper.toEvent(newEventDto, user, location, category, eventDate);
        event.setState(EventStatus.PENDING);
        return ResponseEntity.ok(eventMapper.toEventFullDto(eventRepository.save(event)));
    }

    @Override
    public ResponseEntity<Object> findEventFull(Long userId, Long eventId) {
        log.info("Get event id={} user id={}", eventId, userId);
        commonValidator.validateForUser(userId);
        commonValidator.validateForEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        validateForSomeUser(userId, event);

        return ResponseEntity.ok(eventMapper.toEventFullDto(event));
    }

    @Override
    public ResponseEntity<Object> cancelEventByUser(Long userId, Long eventId) {
        log.info("Cancel event id={} by user id={}", eventId, userId);
        commonValidator.validateForUser(userId);
        commonValidator.validateForEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        validateForFullEvent(userId, event);
        event.setState(EventStatus.CANCELED);
        return ResponseEntity.ok(eventMapper.toEventFullDto(eventRepository.save(event)));
    }

    @Override
    public ResponseEntity<Object> findRequestByUser(Long userId, Long eventId) {
        log.info("Get request by user id={} and event id={}", userId, eventId);
        commonValidator.validateForUser(userId);
        commonValidator.validateForEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        validateForSomeUser(userId, event);
        return ResponseEntity.ok(participationRequestRepository.findAllByEvent(eventId, userId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<Object> approveConfirmUserByEvent(Long userId, Long eventId, Long reqId) {
        log.info("Approve request={} by user id={} and event id={}", reqId, userId, eventId);
        commonValidator.validateForUser(userId);
        commonValidator.validateForEvent(eventId);
        commonValidator.validateForRequest(reqId);
        Event event = eventRepository.findById(eventId).get();

        validateForSomeUser(userId, event);

        ParticipationRequest participationRequest = participationRequestRepository.findById(reqId).get();
        Integer limitParticipant = participationRequestRepository.countByEvent_IdAndStatus(eventId,
                RequestStatus.CONFIRMED);
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(limitParticipant)) {
            participationRequest.setStatus(RequestStatus.REJECTED);
        }
        participationRequest.setStatus(RequestStatus.CONFIRMED);
        return ResponseEntity.ok(
                requestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest)));
    }

    @Override
    public ResponseEntity<Object> approveRejectUserByEvent(Long userId, Long eventId, Long reqId) {
        log.info("Reject request={} by user id={} and event id={}", reqId, userId, eventId);
        commonValidator.validateForUser(userId);
        commonValidator.validateForEvent(eventId);
        commonValidator.validateForRequest(reqId);
        Event event = eventRepository.findById(eventId).get();
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            return null;
        }
        ParticipationRequest participationRequest = participationRequestRepository.findById(reqId).get();
        participationRequest.setStatus(RequestStatus.REJECTED);
        return ResponseEntity.ok(
                requestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest)));
    }

    @Override
    public ResponseEntity<Object> findRequestsByUser(Long userId) {
        log.info("Find all request by id={}", userId);
        commonValidator.validateForUser(userId);
        return ResponseEntity.ok(participationRequestRepository.findAllByRequester_IdOrderById(userId).stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<Object> postRequestUser(Long userId, Long eventId) {
        log.info("Post request by user id={} and event id={}", userId, eventId);
        commonValidator.validateForUser(userId);
        commonValidator.validateForEvent(eventId);
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
            Integer count = participationRequestRepository.countByEvent_IdAndStatus(eventId,
                    RequestStatus.CONFIRMED);
            if (!event.getRequestModeration()) {
                participationRequest.setStatus(RequestStatus.CONFIRMED);
            }
            if (event.getParticipantLimit() != 0 && Objects.equals(event.getParticipantLimit(), count)) {
                participationRequest.setStatus(RequestStatus.REJECTED);
            }
            return ResponseEntity.ok(
                    requestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest)));
        } else {
            throw new ForbiddenRequestException(EVENT_INITIATOR_IS_WRONG);
        }
    }

    @Override
    public ResponseEntity<Object> cancelRequestByUser(Long userId, Long requestId) {
        log.info("Patch request id={} by user id={}", userId, requestId);
        commonValidator.validateForUser(userId);
        commonValidator.validateForRequest(requestId);
        if (!Objects.equals(participationRequestRepository.findById(requestId).get().getRequester().getId(), userId)) {
            throw new ForbiddenRequestException(EVENT_INITIATOR_IS_WRONG);
        }
        ParticipationRequest participationRequest = participationRequestRepository.findById(requestId).get();
        participationRequest.setStatus(RequestStatus.CANCELED);
        return ResponseEntity.ok(
                requestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest)));
    }

    @Override
    public ResponseEntity<Object> findAllUsers(List<Long> ids, Integer from, Integer size) {
        log.info("Admin find all users from={}, size={}", from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        return ResponseEntity.ok(userRepository.findAllByIdOrderByIdDesc(ids, pageable).stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<Object> postUser(NewUserRequest userRequest) {
        log.info("Admin post user");
        User user = userMapper.toUser(userRequest);
        return ResponseEntity.ok(userMapper.toUserDto(userRepository.save(user)));
    }

    @Override
    public ResponseEntity<Object> deleteUser(Long userId) {
        log.info("Admin delete user by id={}", userId);
        commonValidator.validateForUser(userId);
        userRepository.deleteById(userId);
        return ResponseEntity.ok(null);
    }

    private void validateForFullEvent(Long userId, Event event) {
        validateForSomeUser(userId, event);
        if (event.getState().equals(EventStatus.PUBLISHED) || event.getState().equals(EventStatus.CANCELED)) {
            throw new ForbiddenRequestException(EVENT_STATUS_IS_WRONG);
        }
    }

    private void validateForSomeUser(Long userId, Event event) {
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenRequestException(EVENT_INITIATOR_IS_WRONG);
        }
    }

    private void validateForPostEvent(NewEventDto newEventDto, LocalDateTime eventDate) {
        if (!eventDate.isAfter(LocalDateTime.now().minusHours(2))) {
            throw new ForbiddenRequestException(DATE_IS_INVALID);
        }
        if (categoryRepository.findById(Long.valueOf(newEventDto.getCategory())).isEmpty()) {
            throw new ObjectNotFoundException(CATEGORY_NOT_FOUND);
        }
    }

    private void validateForPatchEventByUser(Long userId, Event event) {
        validateForSomeUser(userId, event);
        if (!event.getEventDate().isAfter(LocalDateTime.now().minusHours(2))) {
            throw new ForbiddenRequestException(DATE_IS_INVALID);
        }
        if (event.getState().equals(EventStatus.PUBLISHED)) {
            throw new ForbiddenRequestException(EVENT_STATUS_IS_WRONG);
        }
    }

    private void updateEventFromNewEventDto(UpdateEventRequest newEventDto, Event event) {
        if (newEventDto.getAnnotation() != null) {
            event.setAnnotation(newEventDto.getAnnotation());
        }
        if (newEventDto.getDescription() != null) {
            event.setDescription(newEventDto.getDescription());
        }
        if (newEventDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER));
        }
        if (newEventDto.getPaid() != null) {
            event.setPaid(newEventDto.getPaid());
        }
        if (newEventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(newEventDto.getParticipantLimit());
        }
        if (newEventDto.getTitle() != null) {
            event.setTitle(newEventDto.getTitle());
        }
    }
}
