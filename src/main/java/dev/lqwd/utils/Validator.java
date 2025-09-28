package dev.lqwd.utils;

import dev.lqwd.dto.auth.UserRegistrationRequestDTO;
import dev.lqwd.exception.BadRequestException;
import dev.lqwd.exception.user_validation.UserRegistrationException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public final class Validator {

    private static final String ERROR_MESSAGE_DIFFERENT_PASSWORDS = "Passwords don't match";
    private static final int MAX_LENGTH_URL = 2000;
    private static final String ERROR_MESSAGE_TOO_LONG_URL = "URL length has more than %d symbols: %d";

    public static void validatePasswordOnEquals(UserRegistrationRequestDTO creationRequest) {
        if (!creationRequest.getPassword().equals(creationRequest.getPasswordConfirm())) {
            log.warn(ERROR_MESSAGE_DIFFERENT_PASSWORDS);
            throw new UserRegistrationException(ERROR_MESSAGE_DIFFERENT_PASSWORDS);
        }
    }

    public static void validateUrlLength(int urlLength) {
        if (MAX_LENGTH_URL < urlLength) {
            throw new BadRequestException(ERROR_MESSAGE_TOO_LONG_URL
                    .formatted(MAX_LENGTH_URL, urlLength));
        }
    }

}
