package com.Peabody.deskhub.core.service;

import com.Peabody.deskhub.auth.entity.User;
import com.Peabody.deskhub.auth.repository.UserRepository;
import com.Peabody.deskhub.core.dto.CreateBookingRequest;
import com.Peabody.deskhub.core.entity.Booking;
import com.Peabody.deskhub.core.entity.BookingStatus;
import com.Peabody.deskhub.core.entity.Seat;

import com.Peabody.deskhub.core.exception.*;
import com.Peabody.deskhub.core.repository.BookingRepository;
import com.Peabody.deskhub.core.repository.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public Booking createBooking(
            String employeeId,
            CreateBookingRequest request
    ) {

        User user = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Employee not found"));

        Seat seat = seatRepository.findBySeatNumber(request.seatNumber())
                .orElseThrow(() ->
                        new SeatNotFoundException("Seat not found"));

        validateSeatIsActive(seat);

        validateBookingDate(request.bookingDate());

        validateUserHasNoBooking(
                employeeId,
                request.bookingDate()
        );

        validateSeatAvailability(
                seat.getSeatNumber(),
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

        return booking;
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
            throw new SeatAlreadyOccupiedException(
                    "Seat is already occupied"
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
            throw new DoubleBookingException(
                    "You already booked a seat for this day"
            );
        }
    }

    private void validateSeatAvailability(
            String seatNumber,
            LocalDate bookingDate
    ) {

        boolean seatBooked =
                bookingRepository
                        .existsBySeatSeatNumberAndBookingDateAndStatus(
                                seatNumber,
                                bookingDate,
                                BookingStatus.BOOKED
                        );

        if (seatBooked) {
            throw new DoubleBookingException(
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
                throw new EmployeeIdNotFoundException(
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
            throw new InvalidBookingException(
                    "Bookings are allowed only for next week's Monday to Friday"
            );
        }
    }
}