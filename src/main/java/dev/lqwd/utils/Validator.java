package dev.lqwd.utils;

import dev.lqwd.dto.auth.UserRegistrationRequestDTO;
import dev.lqwd.exception.user_validation.IncorrectCredentialsException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@UtilityClass
@Slf4j
public final class Validator {

    private static final String ERROR_MESSAGE_DIFFERENT_PASSWORDS = "Passwords don't match";

    public static void validatePasswordOnEquals(UserRegistrationRequestDTO creationRequest) {
        if (!creationRequest.getPassword().equals(creationRequest.getPasswordConfirm())) {
            log.warn(ERROR_MESSAGE_DIFFERENT_PASSWORDS);
            throw new IncorrectCredentialsException(ERROR_MESSAGE_DIFFERENT_PASSWORDS);
        }
    }

    public static Optional<UUID> parseUUID(String id) {
        try {
            return Optional.ofNullable(id).map(UUID::fromString);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
