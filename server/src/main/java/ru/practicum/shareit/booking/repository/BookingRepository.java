package ru.practicum.shareit.booking.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

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
}