package dev.lqwd.exception.user_validation;

import lombok.Getter;

@Getter
public class UserRegistrationException extends RuntimeException {

    public UserRegistrationException(String message) {
        super(message);
    }

    public UserRegistrationException(String message, Exception e) {
        super(message, e);
    }

}
