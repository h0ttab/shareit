package com.app.shareit.booking.dto;

import java.time.LocalDateTime;

import com.app.shareit.booking.model.BookingStatus;
import com.app.shareit.item.dto.ItemDto;
import com.app.shareit.user.dto.UserDto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingReturnDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private UserDto booker;
    private ItemDto item;
}