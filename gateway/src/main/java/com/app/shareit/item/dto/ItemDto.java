package com.app.shareit.item.dto;

import java.util.List;

import com.app.shareit.booking.dto.BookingDateDto;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;

    @NotNull(groups = Create.class, message = "Название вещи обязательно для заполнения")
    @Pattern(groups = {Create.class, Update.class}, regexp = ".*\\S.*",
            message = "Название вещи не может быть пустым")
    private String name;

    @NotNull(groups = Create.class, message = "Описание вещи обязательно для заполнения")
    @Pattern(groups = {Create.class, Update.class}, regexp = ".*\\S.*",
            message = "Описание вещи не может быть пустым")
    private String description;

    @NotNull(groups = Create.class, message = "Статус доступности вещи обязателен для заполнения")
    private Boolean available;

    private BookingDateDto lastBooking;

    private BookingDateDto nextBooking;

    private List<CommentDto> comments;

    @Positive
    private Long requestId;

    public interface Create {
    }

    public interface Update {
    }
}
