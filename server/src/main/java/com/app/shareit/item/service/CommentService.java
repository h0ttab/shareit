package com.app.shareit.item.service;

import java.util.List;

import com.app.shareit.item.dto.CommentDto;
import com.app.shareit.item.model.Comment;

public interface CommentService {
    List<CommentDto> getCommentsByItemId(Long itemId);

    List<CommentDto> getCommentsByOwnerId(Long ownerId);

    CommentDto createComment(CommentDto commentDto, Long itemId, Long userId);

    List<Comment> findByItemIdIn(List<Long> itemIds);
}
