package com.app.shareit.booking.dto;

import java.time.LocalDateTime;

import com.app.shareit.util.validation.StartBeforeEnd;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@StartBeforeEnd
public class BookingCreateDto {
    @NotNull(message = "ID бронируемой вещи обязательно для заполнения")
    private Long itemId;

    @NotNull(message = "Дата начала бронирования обязательна для заполнения")
    @FutureOrPresent(message = "Дата начала бронирования не может быть в прошлом")
    private LocalDateTime start;

    @NotNull(message = "Дата окончания бронирования обязательна для заполнения")
    @Future(message = "Дата окончания бронирования не может быть в прошлом")
    private LocalDateTime end;
}
