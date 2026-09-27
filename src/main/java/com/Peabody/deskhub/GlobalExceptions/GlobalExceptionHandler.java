package com.Peabody.deskhub.GlobalExceptions;




import com.Peabody.deskhub.auth.exception.*;
import com.Peabody.deskhub.core.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserExists(
            UserAlreadyExistsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.CONFLICT.value())
                                .error("USER_ALREADY_EXISTS")
                                .message(ex.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.NOT_FOUND.value())
                                .error("USER_NOT_FOUND")
                                .message(ex.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(
            InvalidCredentialsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.CONFLICT.value())
                                .error("INVALID_CREDENTIALS")
                                .message(ex.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(PasswordTooShortException.class)
    public ResponseEntity<ErrorResponse> handlePasswordLengthTooShort(
            PasswordTooShortException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error("PASSWORD_TOO_SHORT")
                                .message(ex.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(SeatAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleSeatAlreadyExists(
            SeatAlreadyExistsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.CONFLICT.value())
                                .error("SEAT_ALREADY_EXISTS")
                                .message(ex.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(SeatNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSeatNotFound(
            SeatNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error("SEAT_NOT_FOUND")
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(EmployeeIdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeIdNotFound(
            EmployeeIdNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error("EMPLOYEE_ID_NOT_FOUND")
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(SeatAlreadyOccupiedException.class)
    public ResponseEntity<ErrorResponse> handleSeatAlreadyOccupied(
            SeatAlreadyOccupiedException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.CONFLICT.value())
                                .error("SEAT_ALREADY_EXISTS")
                                .message(ex.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(DoubleBookingException.class)
    public ResponseEntity<ErrorResponse> handleDoubleBooking(
            DoubleBookingException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.CONFLICT.value())
                                .error("DOUBLE_BOOKING_NOT_ALLOWED")
                                .message(ex.getMessage())
                                .build()
                );
    }
    @ExceptionHandler(InvalidBookingException.class)
    public ResponseEntity<ErrorResponse> handleInvalidBooking(
            InvalidBookingException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(HttpStatus.BAD_REQUEST.value())
                                .error("BOOKING_INVALID")
                                .message(ex.getMessage())
                                .build()
                );
    }





}
