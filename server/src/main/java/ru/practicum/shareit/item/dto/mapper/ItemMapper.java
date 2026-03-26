package ru.practicum.shareit.item.dto.mapper;

import java.util.List;

import org.mapstruct.*;
import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.util.ReferenceMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ReferenceMapper.class)
public interface ItemMapper {
    @Mapping(target = "comments", source = "comments")
    @Mapping(target = "requestId", source = "item.itemRequest.id")
    ItemDto toItemDto(Item item, List<CommentDto> comments);

    ItemDto toItemDtoWithBookings(Item item, List<CommentDto> comments, BookingDateDto lastBooking, BookingDateDto nextBooking);

    @Mapping(target = "owner", source = "userId")
    @Mapping(target = "itemRequest", source = "itemDto.requestId")
    Item fromItemDto(ItemDto itemDto, Long userId);

    List<ItemDto> toItemDtoList(List<Item> itemList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Item updateItemFromDto(ItemDto itemDto, @MappingTarget Item item);

    default Long map(Long id) {
        return id;
    }
}
