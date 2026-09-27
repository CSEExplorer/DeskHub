package com.Peabody.deskhub.core.controller;

import com.Peabody.deskhub.core.dto.CreateBookingRequest;
import com.Peabody.deskhub.core.entity.Booking;
import com.Peabody.deskhub.core.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking Management", description = "APIs for creating and managing office desk bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Operation(
            summary = "Create a new desk booking",
            description = "Allows an authenticated employee to book a desk. Returns the newly created booking ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Booking successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request payload or validation failure", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict - Desk already booked for the selected slot", content = @Content)
    })
    public ResponseEntity<Booking> createBooking(
            @Parameter(hidden = true) Authentication authentication,
            @Valid @RequestBody CreateBookingRequest request
    ) {
        String employeeId = authentication.getName();
        Booking booking = bookingService.createBooking(employeeId, request);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{bookingId}/cancel")
    @Operation(
            summary = "Cancel an existing booking",
            description = "Allows an employee to cancel their own active booking using the booking ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Booking successfully cancelled", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Cannot cancel another employee's booking", content = @Content),
            @ApiResponse(responseCode = "404", description = "Booking not found", content = @Content)
    })
    public ResponseEntity<Void> cancelBooking(
            @Parameter(description = "The unique ID of the booking to be cancelled", required = true, example = "1025")
            @PathVariable Long bookingId,
            @Parameter(hidden = true) Authentication authentication
    ) {
        String employeeId = authentication.getName();
        bookingService.cancelBooking(bookingId, employeeId);
        return ResponseEntity.noContent().build();
    }
}
