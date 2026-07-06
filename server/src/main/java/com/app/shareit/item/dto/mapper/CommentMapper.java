package com.app.shareit.item.dto.mapper;

import java.util.List;

import com.app.shareit.item.dto.CommentDto;
import com.app.shareit.item.model.Comment;
import com.app.shareit.util.ReferenceMapper;
import org.mapstruct.*;

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
