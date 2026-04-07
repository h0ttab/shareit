package ru.practicum.shareit.item.dto.mapper;

import java.util.List;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.util.ReferenceMapper;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = ReferenceMapper.class)
public interface CommentMapper {
    @Mapping(target = "authorName", source = "author.name")
    CommentDto toCommentDto(Comment comment);

    @Mapping(target = "item", source = "itemId")
    @Mapping(target = "author", source = "authorId")
    Comment fromCommentDto(CommentDto commentDto, Long itemId, Long authorId);

    List<CommentDto> toCommentDtoList(List<Comment> commentList);

    default Long map(Long id) {
        return id;
    }
}
