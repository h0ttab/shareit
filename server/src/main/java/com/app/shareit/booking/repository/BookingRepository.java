package com.app.shareit.booking.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.app.shareit.booking.model.Booking;
import com.app.shareit.booking.model.BookingStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = """
            SELECT COUNT(b) = 0
            FROM Booking b
            WHERE b.item.id = :itemId
            AND b.status = 'APPROVED'
            AND b.startDate < :end
            AND b.endDate > :start
            """)
    boolean isItemAvailableDuringDates(@Param("itemId") Long itemId,
                                       @Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end
    );

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByBookerId(Long bookerId, Sort sort);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByBookerIdAndStartDateBeforeAndEndDateAfter(Long bookerId, LocalDateTime now1, LocalDateTime now2, Sort sort);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByBookerIdAndEndDateBefore(Long bookerId, LocalDateTime now, Sort sort);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByBookerIdAndStartDateAfter(Long bookerId, LocalDateTime now, Sort sort);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByItemOwnerId(Long ownerId, Sort sort);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByItemOwnerIdAndStartDateBeforeAndEndDateAfter(Long ownerId, LocalDateTime now1, LocalDateTime now2, Sort sort);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByItemOwnerIdAndEndDateBefore(Long ownerId, LocalDateTime now, Sort sort);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByItemOwnerIdAndStartDateAfter(Long ownerId, LocalDateTime now, Sort sort);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByItemOwnerIdAndStatus(Long ownerId, BookingStatus status, Sort sort);

    boolean existsByBookerIdAndItemIdAndStatusAndEndDateBefore(Long bookerId, Long itemId, BookingStatus bookingStatus, LocalDateTime endTime);

    @Query("""
            SELECT b FROM Booking b
            JOIN FETCH b.item i
            JOIN FETCH b.booker u
            WHERE i.id IN :itemIds
              AND b.status = 'APPROVED'
              AND b.startDate = (
                  SELECT MAX(b2.startDate)
                  FROM Booking b2
                  WHERE b2.item.id = i.id
                    AND b2.status = 'APPROVED'
                    AND b2.startDate <= :now
              )
            """)
    List<Booking> findLastBookings(@Param("itemIds") List<Long> itemIds, @Param("now") LocalDateTime now);

    @Query("""
            SELECT b FROM Booking b
            JOIN FETCH b.item i
            JOIN FETCH b.booker u
            WHERE i.id IN :itemIds
              AND b.status = 'APPROVED'
              AND b.startDate = (
                  SELECT MIN(b2.startDate)
                  FROM Booking b2
                  WHERE b2.item.id = i.id
                    AND b2.status = 'APPROVED'
                    AND b2.startDate > :now
              )
            """)
    List<Booking> findNextBookings(@Param("itemIds") List<Long> itemIds, @Param("now") LocalDateTime now);
}