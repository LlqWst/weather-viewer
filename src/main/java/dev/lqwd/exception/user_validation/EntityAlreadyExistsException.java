package dev.lqwd.exception.user_validation;

public class EntityAlreadyExistsException extends UserValidationException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(String message, Exception e) {
        super(message, e);
    }

}
