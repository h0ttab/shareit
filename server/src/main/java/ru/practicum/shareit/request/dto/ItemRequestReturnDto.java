package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestReturnDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<RequestedItemDto> items;
}