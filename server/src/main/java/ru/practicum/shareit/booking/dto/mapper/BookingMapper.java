package ru.practicum.shareit.booking.dto.mapper;

import java.util.List;

import org.mapstruct.*;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.util.ReferenceMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ReferenceMapper.class)
public interface BookingMapper {
    @Mapping(source = "startDate", target = "start")
    @Mapping(source = "endDate", target = "end")
    BookingReturnDto toBookingReturnDto(Booking booking);

    @Mapping(target = "start", source = "startDate")
    @Mapping(target = "end", source = "endDate")
    BookingDateDto toBookingDateDto(Booking booking);

    @Mapping(source = "dto.itemId", target = "item")
    @Mapping(source = "bookerId", target = "booker")
    @Mapping(source = "dto.start", target = "startDate")
    @Mapping(source = "dto.end", target = "endDate")
    Booking fromBookingCreateDto(BookingCreateDto dto, Long bookerId);

    List<BookingReturnDto> toBookingReturnDtoList(List<Booking> bookingList);

    default Long map(Long id) {
        return id;
    }
}
