package com.Peabody.deskhub.core.service;

import com.Peabody.deskhub.auth.entity.User;
import com.Peabody.deskhub.auth.repository.UserRepository;
import com.Peabody.deskhub.core.dto.CreateBookingRequest;
import com.Peabody.deskhub.core.entity.Booking;
import com.Peabody.deskhub.core.entity.BookingStatus;
import com.Peabody.deskhub.core.entity.Seat;

import com.Peabody.deskhub.core.repository.BookingRepository;
import com.Peabody.deskhub.core.repository.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;

    public Long createBooking(
            String employeeId,
            CreateBookingRequest request
    ) {

        User user = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        Seat seat = seatRepository.findById(request.seatId())
                .orElseThrow(() ->
                        new RuntimeException("Seat not found"));

        validateSeatIsActive(seat);

        validateBookingDate(request.bookingDate());

        validateUserHasNoBooking(
                employeeId,
                request.bookingDate()
        );

        validateSeatAvailability(
                seat.getId(),
                request.bookingDate()
        );

        validateFixedSeatOwnership(
                user,
                seat
        );

        Booking booking = Booking.builder()
                .user(user)
                .seat(seat)
                .bookingDate(request.bookingDate())
                .status(BookingStatus.BOOKED)
                .build();

        bookingRepository.save(booking);

        return booking.getId();
    }

    public void cancelBooking(
            Long bookingId,
            String employeeId
    ) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        if (!booking.getUser()
                .getEmployeeId()
                .equals(employeeId)) {

            throw new RuntimeException(
                    "You can cancel only your own booking"
            );
        }

        booking.setStatus(
                BookingStatus.CANCELLED
        );

        booking.setCancelledAt(
                LocalDateTime.now()
        );
    }

    private void validateSeatIsActive(
            Seat seat
    ) {

        if (!seat.getActive()) {
            throw new RuntimeException(
                    "Seat is inactive"
            );
        }
    }

    private void validateUserHasNoBooking(
            String employeeId,
            LocalDate bookingDate
    ) {

        boolean alreadyBooked =
                bookingRepository
                        .existsByUserEmployeeIdAndBookingDateAndStatus(
                                employeeId,
                                bookingDate,
                                BookingStatus.BOOKED
                        );

        if (alreadyBooked) {
            throw new RuntimeException(
                    "User already booked a seat for this day"
            );
        }
    }

    private void validateSeatAvailability(
            Long seatId,
            LocalDate bookingDate
    ) {

        boolean seatBooked =
                bookingRepository
                        .existsBySeatIdAndBookingDateAndStatus(
                                seatId,
                                bookingDate,
                                BookingStatus.BOOKED
                        );

        if (seatBooked) {
            throw new RuntimeException(
                    "Seat already booked"
            );
        }
    }

    private void validateFixedSeatOwnership(
            User user,
            Seat seat
    ) {

        if (seat.getSeatType() .equals("FIXED")) {

            if (seat.getOwner() == null) {
                throw new RuntimeException(
                        "Fixed seat has no owner configured"
                );
            }

            if (!seat.getOwner()
                    .getEmployeeId()
                    .equals(user.getEmployeeId())) {

                throw new RuntimeException(
                        "You cannot book another employee's fixed seat"
                );
            }
        }
    }

    private void validateBookingDate(
            LocalDate bookingDate
    ) {

        LocalDate today = LocalDate.now();

        LocalDate nextMonday = today
                .with(DayOfWeek.MONDAY)
                .plusWeeks(1);

        LocalDate nextFriday = nextMonday.plusDays(4);

        boolean valid =
                !bookingDate.isBefore(nextMonday)
                        && !bookingDate.isAfter(nextFriday);

        if (!valid) {
            throw new RuntimeException(
                    "Bookings are allowed only for next week's Monday to Friday"
            );
        }
    }
}