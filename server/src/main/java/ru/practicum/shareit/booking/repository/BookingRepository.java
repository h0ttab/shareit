package ru.practicum.shareit.booking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

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

    List<Booking> findByBookerId(Long bookerId, Sort sort);

    List<Booking> findByBookerIdAndStartDateBeforeAndEndDateAfter(Long bookerId, LocalDateTime now1, LocalDateTime now2, Sort sort);

    List<Booking> findByBookerIdAndEndDateBefore(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBookerIdAndStartDateAfter(Long bookerId, LocalDateTime now, Sort sort);

    List<Booking> findByBookerIdAndStatus(Long bookerId, BookingStatus status, Sort sort);

    List<Booking> findByItemOwnerId(Long ownerId, Sort sort);

    List<Booking> findByItemOwnerIdAndStartDateBeforeAndEndDateAfter(Long ownerId, LocalDateTime now1, LocalDateTime now2, Sort sort);

    List<Booking> findByItemOwnerIdAndEndDateBefore(Long ownerId, LocalDateTime now, Sort sort);

    List<Booking> findByItemOwnerIdAndStartDateAfter(Long ownerId, LocalDateTime now, Sort sort);

    List<Booking> findByItemOwnerIdAndStatus(Long ownerId, BookingStatus status, Sort sort);
}