package com.Peabody.deskhub.auth.dto;



import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank
        String employeeId,

        @NotBlank
        String password
) {
}
