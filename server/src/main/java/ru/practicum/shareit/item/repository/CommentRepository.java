package ru.practicum.shareit.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {"author"})
    List<Comment> findAllByItemId(Long itemId);

    @EntityGraph(attributePaths = {"author"})
    List<Comment> findAllByItemOwnerId(Long ownerId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.item.id IN :itemIds")
    List<Comment> findByItemIdIn(@Param("itemIds") List<Long> itemIds);
}