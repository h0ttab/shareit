package ru.practicum.shareit.item.dto;

import java.util.List;

import jakarta.validation.constraints.*;
import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDateDto;

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
