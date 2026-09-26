package com.Peabody.deskhub.core.repository;

import com.Peabody.deskhub.core.entity.BookingStatus;
import com.Peabody.deskhub.core.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findBySeatNumber(String seatNumber);

    List<Seat> findByActiveTrue();

    List<Seat> findBySeatTypeAndActiveTrue(
            String seatType
    );

    @Query("""
            SELECT s
            FROM Seat s
            WHERE s.active = true
            AND s.id NOT IN (
                SELECT b.seat.id
                FROM Booking b
                WHERE b.bookingDate = :bookingDate
                AND b.status = :status
            )
            """)
    List<Seat> findAvailableSeats(
            @Param("bookingDate") LocalDate bookingDate,
            @Param("status") BookingStatus status
    );

    boolean existsBySeatNumber(String seatNumber);
}