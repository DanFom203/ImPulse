package ru.itis.impulse_back.exception;

public class PasswordsNotMatchException extends RuntimeException {
    public PasswordsNotMatchException() {
    }

    public PasswordsNotMatchException(String message) {
        super(message);
    }

    public PasswordsNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
