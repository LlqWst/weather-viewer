package dev.lqwd.utils;

import dev.lqwd.dto.auth.UserRegistrationRequestDTO;
import dev.lqwd.exception.BadRequestException;
import dev.lqwd.exception.user_validation.UserRegistrationException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public final class Validator {

    public static final String LOCATION_PATTERN = "^[a-zA-Zа-яА-Я0-9\\s\\-',.()]{1,100}$";
    private static final String ERROR_MESSAGE_DIFFERENT_PASSWORDS = "Passwords don't match";
    private static final int MAX_LENGTH_URL = 2000;
    private static final String ERROR_MESSAGE_TOO_LONG_URL = "URL length has more than %d symbols: %d";
    public static final String INCORRECT_LOCATION_MESSAGE = "Please use only letters, numbers, spaces, and common punctuation marks (-',.). Maximum 100 characters";

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

    public static void validate(String searched) {
        if (searched == null ||
            searched.isBlank() ||
            !searched.matches(LOCATION_PATTERN)) {

            throw new BadRequestException(INCORRECT_LOCATION_MESSAGE);
        }
    }

}
