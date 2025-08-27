package dev.lqwd;

import dev.lqwd.dto.UserRegistrationRequestDto;
import dev.lqwd.exception.user_validation.IncorrectCredentialsException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public final class Validator {

    private static final String ERROR_MESSAGE_DIFFERENT_PASSWORDS = "Passwords don't match";

    public static void validatePasswordOnEquals(UserRegistrationRequestDto creationRequest){

         if (!creationRequest.getPassword().equals(creationRequest.getPasswordConfirm())){

             log.warn(ERROR_MESSAGE_DIFFERENT_PASSWORDS);
             throw new IncorrectCredentialsException(ERROR_MESSAGE_DIFFERENT_PASSWORDS);
         }
    }

}
