package com.app.shareit.item.service;

import java.time.LocalDateTime;
import java.util.List;

import com.app.shareit.booking.model.BookingStatus;
import com.app.shareit.booking.repository.BookingRepository;
import com.app.shareit.exception.BookingException;
import com.app.shareit.exception.NotFoundException;
import com.app.shareit.item.dto.CommentDto;
import com.app.shareit.item.dto.mapper.CommentMapper;
import com.app.shareit.item.model.Comment;
import com.app.shareit.item.repository.CommentRepository;
import com.app.shareit.item.repository.ItemRepository;
import com.app.shareit.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Primary
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final CommentMapper mapper;
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByItemId(Long itemId) {
        List<Comment> comments = repository.findAllByItemId(itemId);
        return mapper.toCommentDtoList(comments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByOwnerId(Long ownerId) {
        List<Comment> comments = repository.findAllByItemOwnerId(ownerId);
        return mapper.toCommentDtoList(comments);
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Long itemId, Long userId) {
        userService.validateUserExists(userId);
        validateItemExists(itemId);
        validateUserBookedItem(userId, itemId);
        Comment comment = mapper.fromCommentDto(commentDto, itemId, userId);
        comment.setCreated(LocalDateTime.now());
        Comment savedComment = repository.save(comment);
        return mapper.toCommentDto(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findByItemIdIn(List<Long> itemIds) {
        return repository.findByItemIdIn(itemIds);
    }

    private void validateItemExists(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException(String.format("Вещь id=%d не найдена", itemId));
        }
    }

    private void validateUserBookedItem(Long userId, Long itemId) {
        boolean valid = bookingRepository.existsByBookerIdAndItemIdAndStatusAndEndDateBefore(userId, itemId,
                BookingStatus.APPROVED, LocalDateTime.now());
        if (!valid) {
            throw new BookingException(String.format("Для вашей учётной записи не найдено успешно "
                    + "завершённых бронирований вещи id=%d.", itemId));
        }
    }
}
