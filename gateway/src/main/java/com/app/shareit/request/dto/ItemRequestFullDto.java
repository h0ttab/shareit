package com.app.shareit.request.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestFullDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<RequestedItemDto> items;
}