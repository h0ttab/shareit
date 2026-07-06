package com.app.shareit.booking.dto.mapper;

import java.util.List;

import com.app.shareit.booking.dto.*;
import com.app.shareit.booking.model.Booking;
import com.app.shareit.util.ReferenceMapper;
import org.mapstruct.*;

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
