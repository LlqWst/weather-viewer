package dev.lqwd.exception.user_validation;

public class EntityAlreadyExistsException extends UserValidationException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }

}
