package com.Peabody.deskhub.core.controller;



import com.Peabody.deskhub.core.dto.CreateSeatRequest;
import com.Peabody.deskhub.core.dto.UpdateSeatRequest;
import com.Peabody.deskhub.core.entity.Seat;
import com.Peabody.deskhub.core.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
@Tag(name = "Seat Management", description = "Endpoints for creating, updating, and filtering workspace seats")
public class SeatController {

    private final SeatService seatService;

    @PostMapping
    @Operation(summary = "Create a new seat", description = "Adds a new desk or seat to the office registry.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seat created successfully",
                    content = @Content(schema = @Schema(implementation = Seat.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body supplied")
    })
    public ResponseEntity<Seat> createSeat(
            @Valid @RequestBody CreateSeatRequest request
    ) {
        return ResponseEntity.ok(
                seatService.createSeat(request)
        );
    }

    @PutMapping("/{seatId}")
    @Operation(summary = "Update an existing seat", description = "Updates structural or status attributes of a specific seat by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seat updated successfully",
                    content = @Content(schema = @Schema(implementation = Seat.class))),
            @ApiResponse(responseCode = "404", description = "Seat not found with the provided ID")
    })
    public ResponseEntity<Seat> updateSeat(
            @Parameter(description = "ID of the seat to update", required = true, example = "1")
            @PathVariable Long seatId,
            @Valid @RequestBody UpdateSeatRequest request
    ) {
        return ResponseEntity.ok(
                seatService.updateSeat(seatId, request)
        );
    }

    @GetMapping
    @Operation(summary = "Get all active seats", description = "Retrieves a complete list of currently active seats in the system.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved active seats list")
    public ResponseEntity<List<Seat>> getAllActiveSeats() {
        return ResponseEntity.ok(
                seatService.getAllActiveSeats()
        );
    }

    @GetMapping("/available")
    @Operation(summary = "Get available seats for a specific date", description = "Filters and returns unbooked seats for the specified calendar date.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved available seats list")
    public ResponseEntity<List<Seat>> getAvailableSeats(
            @Parameter(description = "Target booking date (ISO Format: YYYY-MM-DD)", required = true, example = "2026-10-15")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate bookingDate
    ) {
        return ResponseEntity.ok(
                seatService.getAvailableSeats(bookingDate)
        );
    }

    @GetMapping("/fixed")
    @Operation(summary = "Get all fixed seats", description = "Retrieves a list of all assigned permanent/fixed seats.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved fixed seats list")
    public ResponseEntity<List<Seat>> getFixedSeats() {
        return ResponseEntity.ok(
                seatService.getFixedSeats()
        );
    }

    @GetMapping("/flexible")
    @Operation(summary = "Get all flexible seats", description = "Retrieves a list of all hot-desking/flexible seats.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved flexible seats list")
    public ResponseEntity<List<Seat>> getFlexibleSeats() {
        return ResponseEntity.ok(
                seatService.getFlexibleSeats()
        );
    }
}
