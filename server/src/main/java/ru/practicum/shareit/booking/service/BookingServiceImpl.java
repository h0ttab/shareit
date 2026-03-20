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

@Service
@Primary
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImpl implements BookingService {
    private final BookingMapper mapper;
    private final BookingRepository repository;

    @Override
    public BookingReturnDto createBooking(BookingCreateDto dto, Long bookerId) {
        Booking booking = mapper.fromBookingCreateDto(dto, bookerId);
        booking.setStatus(BookingStatus.WAITING);
        Booking created = repository.save(booking);
        return mapper.toBookingReturnDto(created);
    }
}
