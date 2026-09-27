package com.Peabody.deskhub.core.service;


import com.Peabody.deskhub.auth.entity.User;
import com.Peabody.deskhub.auth.repository.UserRepository;
import com.Peabody.deskhub.core.dto.CreateSeatRequest;
import com.Peabody.deskhub.core.dto.UpdateSeatRequest;
import com.Peabody.deskhub.core.entity.BookingStatus;
import com.Peabody.deskhub.core.entity.Seat;
import com.Peabody.deskhub.core.exception.EmployeeIdNotFoundException;
import com.Peabody.deskhub.core.exception.SeatAlreadyExistsException;
import com.Peabody.deskhub.core.exception.SeatNotFoundException;
import com.Peabody.deskhub.core.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final UserRepository userRepository;

    public Seat createSeat(CreateSeatRequest request) {

        if (seatRepository.existsBySeatNumber(request.seatNumber())) {
            throw new SeatAlreadyExistsException("Seat number already exists");
        }
        Seat seat = Seat.builder()
                .seatNumber(request.seatNumber())
                .seatType(request.seatType().toUpperCase())
                .owner(resolveOwner(
                        request.seatType(),
                        request.ownerEmployeeId()
                ))
                .active(true)
                .build();
        return seatRepository.save(seat);
    }
    public Seat updateSeat(
            String seatId,
            UpdateSeatRequest request
    ) {

        Seat seat = seatRepository.findBySeatNumber(seatId)
                .orElseThrow(() ->
                        new SeatNotFoundException("Seat not found"));

        if (request.seatNumber() != null
                && !request.seatNumber().equals(seat.getSeatNumber())) {

            if (seatRepository.existsBySeatNumber(request.seatNumber())) {
                throw new SeatAlreadyExistsException("Seat number already exists");
            }

            seat.setSeatNumber(request.seatNumber());
        }

        if (request.seatType() != null) {

            seat.setSeatType(
                    request.seatType().toUpperCase()
            );

            seat.setOwner(
                    resolveOwner(
                            request.seatType(),
                            request.ownerEmployeeId()
                    )
            );
        }

        if (request.active() != null) {
            seat.setActive(request.active());
        }

        return seatRepository.save(seat);
    }

    public List<Seat> getAllActiveSeats() {

        return seatRepository.findByActiveTrue();
    }

    public List<Seat> getAvailableSeats(
            LocalDate bookingDate
    ) {

        return seatRepository.findAvailableSeats(
                bookingDate,
                BookingStatus.BOOKED
        );
    }

    public List<Seat> getFixedSeats() {

        return seatRepository.findBySeatTypeAndActiveTrue(
                "FIXED"
        );
    }

    public List<Seat> getFlexibleSeats() {

        return seatRepository.findBySeatTypeAndActiveTrue(
                "FLEXIBLE"
        );
    }

    private User resolveOwner(
            String seatType,
            String ownerEmployeeId
    ) {

        if (!"FIXED".equalsIgnoreCase(seatType)) {
            return null;
        }

        if (ownerEmployeeId == null || ownerEmployeeId.isBlank()) {
            throw new EmployeeIdNotFoundException(
                    "Fixed seat requires an owner employee id"
            );
        }
       return userRepository.findByEmployeeId(ownerEmployeeId)
                .orElseThrow(() ->
                        new EmployeeIdNotFoundException(
                                "Owner employee not found"
                        ));
    }


}