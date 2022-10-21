package ru.practicum.ewm.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventStatus;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exc.ForbiddenRequestException;
import ru.practicum.ewm.exc.ObjectNotFoundException;
import ru.practicum.ewm.user.dto.NewUserRequest;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.validator.CommonValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final CategoryRepository categoryRepository;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final CompilationMapper compilationMapper;
    private final CommonValidator commonValidator;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String MESSAGE = "Error";

    @Override
    public Collection<EventFullDto> findAllEvents(Map<String, Object> parameters) {
        Pageable pageable = PageRequest.of((Integer) parameters.get("from") / (Integer) parameters.get("size"),
                (Integer) parameters.get("size"));
        List<Long> users = (List<Long>) parameters.get("users");
        List<EventStatus> states = (List<EventStatus>) parameters.get("states");
        List<Long> catIds = (List<Long>) parameters.get("categories");
        LocalDateTime rangeStart = LocalDateTime.parse((String) parameters.get("rangeStart"), FORMATTER);
        LocalDateTime rangeEnd = LocalDateTime.parse((String) parameters.get("rangeEnd"), FORMATTER);
        return eventRepository.findAllEvents(users, states, catIds, rangeStart, rangeEnd, pageable).stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto putEvent(long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        commonValidator.eventValidator(eventId);
        LocalDateTime eventDate = LocalDateTime.parse(adminUpdateEventRequest.getEventDate(),
                FORMATTER);
        if (!eventDate.isAfter(LocalDateTime.now().minusHours(2))) {
            throw new ForbiddenRequestException(MESSAGE);
        }
        Event event = eventRepository.findById(eventId).get();
        if (adminUpdateEventRequest.getCategory() != null) {
            if (categoryRepository.findById(Long.valueOf(adminUpdateEventRequest.getCategory())).isEmpty()) {
                throw new ObjectNotFoundException(MESSAGE);
            }
            Category category = categoryRepository.findById(Long.valueOf(adminUpdateEventRequest.getCategory())).get();
            event.setCategory(category);
            event.setEventDate(eventDate);
        }
        eventMapper.updateEventFromAdminUpdateEventRequest(adminUpdateEventRequest, event);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto approvePublishEvent(long eventId) {
        commonValidator.eventValidator(eventId);
        Event event = eventRepository.findById(eventId).get();
        event.setState(EventStatus.PUBLISHED);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto approveRejectEvent(long eventId) {
        commonValidator.eventValidator(eventId);
        Event event = eventRepository.findById(eventId).get();
        event.setState(EventStatus.CANCELED);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public CategoryDto patchCategory(CategoryDto categoryDto) {
        commonValidator.categoryValidator(categoryDto.getId());
        if (categoryRepository.findFirstByName(categoryDto.getName()).isPresent()) {
            throw new ForbiddenRequestException(MESSAGE);
        }
        Category category = categoryRepository.findById(categoryDto.getId()).get();
        categoryMapper.updateCategoryFromCategoryDto(categoryDto, category);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto postCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.toCategory(newCategoryDto);
        CategoryDto categoryDto = categoryMapper.toCategoryDto(categoryRepository.save(category));
        return categoryDto;
    }

    @Override
    public void deleteCategory(long catId) {
        commonValidator.categoryValidator(catId);
        categoryRepository.deleteById(catId);
    }

    @Override
    public Collection<UserDto> findAllUsers(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Collection<UserDto> userDtoCollection = userRepository.findAllByIdOrderByIdDesc(ids, pageable).stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
        return userDtoCollection;
    }

    @Override
    public UserDto postUser(NewUserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        UserDto userDto = userMapper.toUserDto(userRepository.save(user));
        return userDto;
    }

    @Override
    public void deleteUser(long userId) {
        commonValidator.userValidator(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public void pinCompilation(long compId) {
        commonValidator.compilationValidator(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpinCompilation(long compId) {
        commonValidator.compilationValidator(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void addEventInCompilation(long compId, long eventId) {
        commonValidator.compilationValidator(compId);
        commonValidator.eventValidator(eventId);
        Compilation compilation = compilationRepository.findById(compId).get();
        Event event = eventRepository.findById(eventId).get();
        if (compilation.getEvents().contains(event)) {
            return;
        }
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void deleteEventInCompilation(long compId, long eventId) {
        commonValidator.compilationValidator(compId);
        commonValidator.eventValidator(eventId);
        Compilation compilation = compilationRepository.findById(compId).get();
        Event event = eventRepository.findById(eventId).get();
        if (!compilation.getEvents().contains(event)) {
            return;
        }
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto, events);
        compilationRepository.save(compilation);
        List<EventShortDto> eventShortDtoList = events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return compilationMapper.toCompilationDto(compilation, eventShortDtoList);
    }


    //TODO
    @Override
    public void deleteCompilation(long compId) {
        commonValidator.compilationValidator(compId);
        compilationRepository.deleteById(compId);
    }
}
