package com.app.shareit.request.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.app.shareit.request.dto.*;
import com.app.shareit.request.model.ItemRequest;
import com.app.shareit.util.ReferenceMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = ReferenceMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {ArrayList.class})
public interface ItemRequestMapper {

    @Mapping(target = "requestor", source = "userId")
    ItemRequest fromDto(ItemRequestCreateDto dto, Long userId);

    ItemRequestFullDto toDto(ItemRequest itemRequest, List<RequestedItemDto> items);

    ItemRequestLightDto toDto(ItemRequest itemRequest);

    List<ItemRequestLightDto> toDtoList(List<ItemRequest> itemRequests);

    default Long map(Long id) {
        return id;
    }
}
