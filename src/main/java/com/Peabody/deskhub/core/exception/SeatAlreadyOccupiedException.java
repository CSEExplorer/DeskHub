package com.Peabody.deskhub.core.exception;

public class SeatAlreadyOccupiedException extends RuntimeException {
    public SeatAlreadyOccupiedException(String message) {
        super(message);
    }
}
