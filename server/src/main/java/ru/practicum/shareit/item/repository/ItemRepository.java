package ru.practicum.shareit.item.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId);

    @Query("""
            SELECT i
            FROM Item i
            WHERE i.available = true
            AND (
                UPPER(i.name) LIKE CONCAT('%', :query, '%')
                OR UPPER(i.description) LIKE CONCAT('%', :query, '%')
            )
            """)
    List<Item> searchAvailable(@Param("query") String query);


    @Query(value = """
            SELECT i.owner.id
            FROM Item i
            WHERE i.id = :itemId
            """)
    Long findOwnerIdByItemId(@Param("itemId") Long itemId);
}