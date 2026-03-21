package ru.practicum.shareit.booking.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

@Service
@Primary
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingMapper mapper;
    private final BookingRepository repository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public BookingReturnDto createBooking(BookingCreateDto dto, Long bookerId) {
        validateBookingRequest(dto, bookerId);
        Booking booking = mapper.fromBookingCreateDto(dto, bookerId);
        booking.setStatus(BookingStatus.WAITING);
        Booking created = repository.save(booking);
        return mapper.toBookingReturnDto(created);
    }

    @Override
    public BookingReturnDto approveBooking(Long bookingId, Long userId, Boolean isApproved) {
        Booking booking = getBookingIfExists(bookingId);
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new BookingException(String.format("Бронирование id=%d уже подтверждено", bookingId));
        }
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new OwnerMismatchException(
                    String.format("Пользователь id=%d не является владельцем забронированной вещи", userId)
            );
        }
        if (isApproved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return mapper.toBookingReturnDto(repository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingReturnDto getBookingById(Long userId, Long bookingId) {
        userService.validateUserExists(userId);
        Booking booking = getBookingIfExists(bookingId);
        Long itemOwnerId = booking.getItem().getOwner().getId();
        if (userId.equals(itemOwnerId) || userId.equals(booking.getBooker().getId())) {
            return mapper.toBookingReturnDto(booking);
        }
        throw new OwnerMismatchException("Просмотр этого бронирования недоступен для вашей учётной записи");
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBookingIfExists(Long bookingId) {
        return repository
                .findById(bookingId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Бронирование id=%d не найдено", bookingId)
                        )
                );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isItemAvailableDuringDates(Long itemId, LocalDateTime start, LocalDateTime end) {
        return repository.isItemAvailableDuringDates(itemId, start, end);
    }

    @Override
    public void validateBookingRequest(BookingCreateDto dto, Long bookerId) {
        ItemDto item = itemService.getById(dto.getItemId());
        userService.validateUserExists(bookerId);
        if (Objects.equals(itemService.getOwnerIdByItemId(item.getId()), bookerId)) {
            throw new OwnerMismatchException("Владелец не может бронировать собственную вещь.");
        }
        if (!item.getAvailable()) {
            throw new ItemUnavailableException(String.format("Вещь id=%d недоступна для бронирования", item.getId()));
        }
        if (!isItemAvailableDuringDates(item.getId(), dto.getStart(), dto.getEnd())) {
            throw new ItemUnavailableException("Выбранный интервал бронирования недоступен");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingReturnDto> getBookingsByBooker(Long bookerId, String stateParam) {
        userService.validateUserExists(bookerId);

        BookingState state = BookingState.valueOf(stateParam);
        LocalDateTime now = LocalDateTime.now();
        Sort sort = Sort.by(Sort.Direction.DESC, "startDate");

        List<Booking> bookings = switch (state) {
            case ALL -> repository.findByBookerId(bookerId, sort);
            case CURRENT -> repository.findByBookerIdAndStartDateBeforeAndEndDateAfter(bookerId, now, now, sort);
            case PAST -> repository.findByBookerIdAndEndDateBefore(bookerId, now, sort);
            case FUTURE -> repository.findByBookerIdAndStartDateAfter(bookerId, now, sort);
            case WAITING -> repository.findByBookerIdAndStatus(bookerId, BookingStatus.WAITING, sort);
            case REJECTED -> repository.findByBookerIdAndStatus(bookerId, BookingStatus.REJECTED, sort);
        };

        return mapper.toBookingReturnDtoList(bookings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingReturnDto> getBookingsByOwner(Long ownerId, String stateParam) {
        userService.validateUserExists(ownerId);

        BookingState state = BookingState.valueOf(stateParam);
        LocalDateTime now = LocalDateTime.now();
        Sort sort = Sort.by(Sort.Direction.DESC, "startDate");

        List<Booking> bookings = switch (state) {
            case ALL -> repository.findByItemOwnerId(ownerId, sort);
            case CURRENT -> repository.findByItemOwnerIdAndStartDateBeforeAndEndDateAfter(ownerId, now, now, sort);
            case PAST -> repository.findByItemOwnerIdAndEndDateBefore(ownerId, now, sort);
            case FUTURE -> repository.findByItemOwnerIdAndStartDateAfter(ownerId, now, sort);
            case WAITING -> repository.findByItemOwnerIdAndStatus(ownerId, BookingStatus.WAITING, sort);
            case REJECTED -> repository.findByItemOwnerIdAndStatus(ownerId, BookingStatus.REJECTED, sort);
        };

        return mapper.toBookingReturnDtoList(bookings);
    }
}
