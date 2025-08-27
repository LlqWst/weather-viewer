package dev.lqwd.exception.user_validation;

public class IncorrectCredentialsException extends UserValidationException {
    public IncorrectCredentialsException(String message) {
        super(message);
    }

}
