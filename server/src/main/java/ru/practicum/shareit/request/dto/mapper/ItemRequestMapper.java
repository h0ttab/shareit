package ru.practicum.shareit.request.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.*;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.util.ReferenceMapper;

@Mapper(componentModel = "spring",
        uses = ReferenceMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {ArrayList.class})
public interface ItemRequestMapper {

    @Mapping(target = "requestor", source = "userId")
    ItemRequest fromDto(ItemRequestCreateDto dto, Long userId);

    ItemRequestReturnDto toDto(ItemRequest itemRequest, List<RequestedItemDto> items);

    default Long map(Long id) {
        return id;
    }
}
