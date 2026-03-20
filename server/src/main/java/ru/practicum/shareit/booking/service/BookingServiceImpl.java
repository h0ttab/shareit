package ru.practicum.shareit.booking.service;

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
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.OwnerMismatchException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

@Service
@Primary
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImpl implements BookingService {
    private final BookingMapper mapper;
    private final BookingRepository repository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public BookingReturnDto createBooking(BookingCreateDto dto, Long bookerId) {
        if (!userService.existsById(bookerId)) {
            throw new NotFoundException(
                    String.format("Пользователь id=%d не найден", bookerId)
            );
        }
        if (!itemService.existsById(dto.getItemId())) {
            throw new NotFoundException(
                    String.format("Вещь id=%d не найдена", dto.getItemId())
            );
        }
        Booking booking = mapper.fromBookingCreateDto(dto, bookerId);
        booking.setStatus(BookingStatus.WAITING);
        Booking created = repository.save(booking);
        return mapper.toBookingReturnDto(created);
    }

    @Override
    public BookingReturnDto approveBooking(Long bookingId, Long userId, Boolean isApproved) {
        Booking booking = repository
                .findById(bookingId)
                .orElseThrow(()->
                        new NotFoundException(
                                String.format("Бронирование id=%d не найдено", bookingId)
                        )
                );
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
}
