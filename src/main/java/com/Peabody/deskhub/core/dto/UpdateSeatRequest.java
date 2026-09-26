package com.Peabody.deskhub.core.dto;



public record UpdateSeatRequest(

        String seatNumber,

        String seatType,

        String ownerEmployeeId,

        Boolean active
) {
}
