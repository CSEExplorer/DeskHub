package com.Peabody.deskhub.auth.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.Generated;


public record RegisterRequest(

        @NotBlank
        String employeeId,

        @NotBlank
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@coforge\\.com$",
                message = "Email must belong to coforge.com domain"
        )
        String email,

        @NotBlank
        String password
) {
}