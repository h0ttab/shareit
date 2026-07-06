package com.app.shareit.item.dto;

import java.util.List;

import com.app.shareit.booking.dto.BookingDateDto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDateDto lastBooking;
    private BookingDateDto nextBooking;
    private List<CommentDto> comments;
    private Long requestId;
}
