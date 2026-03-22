package ru.practicum.shareit.item.dto.mapper;

import java.util.List;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.util.ReferenceMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ReferenceMapper.class)
public interface ItemMapper {
    @Mapping(target = "comments", source = "comments")
    ItemDto toItemDto(Item item, List<CommentDto> comments);

    @Mapping(target = "owner", source = "userId")
    Item fromItemDto(ItemDto itemDto, Long userId);

    List<ItemDto> toItemDtoList(List<Item> itemList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Item updateItemFromDto(ItemDto itemDto, @MappingTarget Item item);

    default Long map(Long id) {
        return id;
    }
}
