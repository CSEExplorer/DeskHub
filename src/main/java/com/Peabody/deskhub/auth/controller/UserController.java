package com.Peabody.deskhub.auth.controller;

import com.Peabody.deskhub.auth.dto.LoginRequest;
import com.Peabody.deskhub.auth.dto.RegisterRequest;
import com.Peabody.deskhub.auth.service.Impl.AuthServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Endpoints for employee registration, session management, and authentication handling via secure HTTP-Only cookies."
)
public class UserController {

    private final AuthServiceImpl authService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Register a new employee",
            description = "Creates a new employee account in DeskHub system. Validates user input before registration."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee successfully registered.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\"data\": {\"id\": 1, \"email\": \"user@peabody.com\"}, \"message\": \"Employee registered successfully.\"}")
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request payload or validation failed.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict - Email or Username already exists.", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        Map<String, Object> res = authService.register(request);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("data", res);
        responseBody.put("message", "Employee registered successfully.");

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping(value = "/login", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
            summary = "Authenticate employee",
            description = "Validates user credentials. Upon success, issues a secure JWT token wrapped inside an HttpOnly cookie."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful. 'access_token' cookie set.",
                    headers = @io.swagger.v3.oas.annotations.headers.Header(
                            name = HttpHeaders.SET_COOKIE,
                            description = "Contains the HttpOnly access_token JWT string."
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid email or password.", content = @Content)
    })
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequest request
    ) {
        String accessToken = authService.login(request);

        ResponseCookie cookie = ResponseCookie
                .from("access_token", accessToken)
                .httpOnly(true)
                .secure(false) // Set to true in production with HTTPS
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMinutes(60))
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Login successful.");
    }

    @PostMapping(value = "/logout", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(
            summary = "Log out current employee",
            description = "Clears the active session by invalidating and expiring the client-side 'access_token' cookie."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Logout successful. Cookie removed.",
                    headers = @io.swagger.v3.oas.annotations.headers.Header(
                            name = HttpHeaders.SET_COOKIE,
                            description = "Clears access_token value and sets maxAge to 0."
                    )
            )
    })
    public ResponseEntity<String> logout() {
        ResponseCookie cookie = ResponseCookie
                .from("access_token", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logout successful.");
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get current logged-in employee profile",
            description = "Retrieves the full profile details of the currently authenticated employee using the security context session."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile details retrieved successfully.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\"data\": {\"id\": 1, \"name\": \"John Doe\", \"email\": \"johndoe@peabody.com\", \"role\": \"EMPLOYEE\"}, \"message\": \"Profile retrieved successfully.\"}")
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Valid JWT cookie is missing or has expired.", content = @Content)
    })
    public ResponseEntity<Map<String, Object>> getCurrentEmployee(
            org.springframework.security.core.Authentication authentication
    ) {
        // Safe check: If authentication is null, the user is unauthenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        // Extracts the subject identifier (e.g., email or username) from your JWT filter context
        String identifier = authentication.getName();



        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("data", identifier);
        responseBody.put("message", "Profile retrieved successfully.");

        return ResponseEntity.ok(responseBody);
    }

}
