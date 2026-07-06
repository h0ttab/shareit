package com.app.shareit.item.repository;

import java.util.List;

import com.app.shareit.item.model.Comment;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {"author"})
    List<Comment> findAllByItemId(Long itemId);

    @EntityGraph(attributePaths = {"author"})
    List<Comment> findAllByItemOwnerId(Long ownerId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.item.id IN :itemIds")
    List<Comment> findByItemIdIn(@Param("itemIds") List<Long> itemIds);
}