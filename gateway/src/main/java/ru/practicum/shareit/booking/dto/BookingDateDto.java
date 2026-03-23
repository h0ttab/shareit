package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDateDto {
    private LocalDateTime start;
    private LocalDateTime end;
}
