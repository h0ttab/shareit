package ru.practicum.shareit.booking.service;

import java.time.LocalDateTime;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

@Service
@Primary
@RequiredArgsConstructor(onConstructor_ = @Autowired)
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
        Booking booking = repository
                .findById(bookingId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Бронирование id=%d не найдено", bookingId)
                        )
                );
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
}
