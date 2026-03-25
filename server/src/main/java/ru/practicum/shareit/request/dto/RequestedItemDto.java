package ru.practicum.shareit.request.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestedItemDto {
    private Long itemId;
    private String name;
    private Long ownerId;
}
