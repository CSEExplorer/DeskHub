package com.Peabody.deskhub.auth.exception;

public class PasswordTooShortException extends RuntimeException{
    public PasswordTooShortException(String message) {
        super(message);
    }
}
