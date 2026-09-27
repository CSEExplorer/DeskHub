package com.Peabody.deskhub.core.repository;

import com.Peabody.deskhub.core.entity.Booking;
import com.Peabody.deskhub.core.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByUserEmployeeIdAndBookingDateAndStatus(
            String employeeId,
            LocalDate bookingDate,
            BookingStatus status
    );

    boolean existsBySeatSeatNumberAndBookingDateAndStatus(
            String seatNumber,
            LocalDate bookingDate,
            BookingStatus status
    );

    List<Booking> findByBookingDateAndStatus(
            LocalDate bookingDate,
            BookingStatus status
    );

    Optional<Booking> findByIdAndStatus(
            Long id,
            BookingStatus status
    );

    List<Booking> findByUserEmployeeId(
            String employeeId
    );

    List<Booking> findBySeatSeatNumber(
            String seatNumber
    );

    List<Booking> findByUserEmployeeIdAndStatus(
            String employeeId,
            BookingStatus status
    );

    List<Booking> findBySeatSeatNumberAndStatus(
            String seatNumber,
            BookingStatus status
    );
}
