package ru.practicum.explore.services.privateApi.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.models.error.ForbiddenRequestException;
import ru.practicum.explore.models.error.ObjectNotFoundException;
import ru.practicum.explore.errors.validate.ObjectValidate;
import ru.practicum.explore.dto.category.CategoryDto;
import ru.practicum.explore.dto.category.NewCategoryDto;
import ru.practicum.explore.mappers.category.CategoryMapper;
import ru.practicum.explore.models.category.Category;
import ru.practicum.explore.repositories.category.CategoryRepository;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.NewCompilationDto;
import ru.practicum.explore.mappers.compilation.CompilationMapper;
import ru.practicum.explore.models.compilation.Compilation;
import ru.practicum.explore.repositories.compilation.CompilationRepository;
import ru.practicum.explore.dto.event.AdminUpdateEventRequest;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.mappers.event.EventMapper;
import ru.practicum.explore.models.event.Event;
import ru.practicum.explore.repositories.event.EventRepository;
import ru.practicum.explore.statuses.event.Status;
import ru.practicum.explore.dto.user.NewUserRequest;
import ru.practicum.explore.dto.user.UserDto;
import ru.practicum.explore.mappers.user.UserMapper;
import ru.practicum.explore.models.user.User;
import ru.practicum.explore.repositories.user.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    private CategoryRepository categoryRepository;
    private CompilationRepository compilationRepository;
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private EventMapper eventMapper;
    private CategoryMapper categoryMapper;
    private UserMapper userMapper;
    private CompilationMapper compilationMapper;
    private ObjectValidate objectValidate;

    @Autowired
    public AdminServiceImpl(CategoryRepository categoryRepository, CompilationRepository compilationRepository,
                            EventRepository eventRepository, UserRepository userRepository, EventMapper eventMapper,
                            CategoryMapper categoryMapper, UserMapper userMapper, CompilationMapper compilationMapper,
                            ObjectValidate objectValidate) {
        this.categoryRepository = categoryRepository;
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.eventMapper = eventMapper;
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
        this.compilationMapper = compilationMapper;
        this.objectValidate = objectValidate;
    }

    @Override
    public Collection<EventFullDto> getAllEvents(Map<String, Object> parameters) {
        Pageable pageable = PageRequest.of((Integer) parameters.get("from") / (Integer) parameters.get("size"),
                (Integer) parameters.get("size"));
        List<Long> users = (List<Long>) parameters.get("users");
        List<Status> states = (List<Status>) parameters.get("states");
        List<Long> catIds = (List<Long>) parameters.get("categories");
        LocalDateTime rangeStart = LocalDateTime.parse((String) parameters.get("rangeStart"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime rangeEnd = LocalDateTime.parse((String) parameters.get("rangeEnd"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Collection<EventFullDto> fullDtoCollection =
                eventRepository.getAllEvents(users, states, catIds, rangeStart, rangeEnd, pageable).stream()
                        .map(eventMapper::toEventFullDto)
                        .collect(Collectors.toList());
        return fullDtoCollection;
    }

    @Override
    public EventFullDto putEvent(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        objectValidate.validateEvent(eventId);
        LocalDateTime eventDate = LocalDateTime.parse(adminUpdateEventRequest.getEventDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (!eventDate.isAfter(LocalDateTime.now().minusHours(2))) {
            throw new ForbiddenRequestException(String.format("Bad date."));
        }
        Event event = eventRepository.findById(eventId).get();
        if (adminUpdateEventRequest.getCategory() != null) {
            if (!categoryRepository.findById(Long.valueOf(adminUpdateEventRequest.getCategory())).isPresent()) {
                throw new ObjectNotFoundException(String.format("Category not found."));
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
    public EventFullDto approvePublishEvent(Long eventId) {
        objectValidate.validateEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        event.setState(Status.PUBLISHED);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto approveRejectEvent(Long eventId) {
        objectValidate.validateEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        event.setState(Status.CANCELED);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public CategoryDto patchCategory(CategoryDto categoryDto) {
        objectValidate.validateCategory(categoryDto.getId());
        if (categoryRepository.findFirstByName(categoryDto.getName()).isPresent()) {
            throw new ForbiddenRequestException(String.format("Bad name"));
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
    public void deleteCategory(Long catId) {
        objectValidate.validateCategory(catId);
        categoryRepository.deleteById(catId);
    }

    @Override
    public Collection<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size) {
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
    public void deleteUser(Long userId) {
        objectValidate.validateUser(userId);
        userRepository.deleteById(userId);
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

    @Override
    public void deleteCompilation(Long compId) {
        objectValidate.validateCompilation(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEventInCompilation(Long compId, Long eventId) {
        objectValidate.validateCompilation(compId);
        objectValidate.validateEvent(eventId);
        Compilation compilation = compilationRepository.findById(compId).get();
        Event event = eventRepository.findById(eventId).get();
        if (!compilation.getEvents().contains(event)) {
            return;
        }
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void addEventInCompilation(Long compId, Long eventId) {
        objectValidate.validateCompilation(compId);
        objectValidate.validateEvent(eventId);
        Compilation compilation = compilationRepository.findById(compId).get();
        Event event = eventRepository.findById(eventId).get();
        if (compilation.getEvents().contains(event)) {
            return;
        }
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpinCompilation(Long compId) {
        objectValidate.validateCompilation(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void pinCompilation(Long compId) {
        objectValidate.validateCompilation(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }
}
