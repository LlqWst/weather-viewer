package dev.lqwd.exception.user_validation;

public class UserAlreadyExistsException extends UserValidationException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
