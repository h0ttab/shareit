package ru.practicum.shareit.item.dto;

import java.util.List;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDateDto;

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
