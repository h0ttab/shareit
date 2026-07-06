package com.app.shareit.request.dto;

import java.time.LocalDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestLightDto {
    private Long id;
    private String description;
    private LocalDateTime created;
}
