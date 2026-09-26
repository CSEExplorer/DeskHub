package com.Peabody.deskhub.core.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSeatRequest(

        @NotBlank
        String seatNumber,

        @NotBlank
        String seatType,

        String ownerEmployeeId
) {
}