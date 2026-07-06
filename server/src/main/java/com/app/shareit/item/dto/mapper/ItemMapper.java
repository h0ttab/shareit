package com.app.shareit.item.dto.mapper;

import java.util.List;

import com.app.shareit.booking.dto.BookingDateDto;
import com.app.shareit.item.dto.CommentDto;
import com.app.shareit.item.dto.ItemDto;
import com.app.shareit.item.model.Item;
import com.app.shareit.util.ReferenceMapper;
import org.mapstruct.*;

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
