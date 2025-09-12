package dev.lqwd.exception.user_validation;

public class UserValidationException extends RuntimeException {
    public UserValidationException(String message, Exception e) {
        super(message, e);
    }

    public UserValidationException(String message) {
        super(message);
    }
}
