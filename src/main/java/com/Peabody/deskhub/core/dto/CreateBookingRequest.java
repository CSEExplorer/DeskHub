package com.Peabody.deskhub.core.dto;



import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateBookingRequest(

        @NotNull
        String seatNumber,

        @NotNull
        LocalDate bookingDate

) {
}