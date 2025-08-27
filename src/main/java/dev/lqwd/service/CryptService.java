package dev.lqwd.service;

import dev.lqwd.exception.user_validation.IncorrectCredentialsException;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CryptService {

    private static final int DEFAULT_LOG_ROUNDS = 12;
    private static final String ERROR_MESSAGE = "Incorrect Password";

    public String getHashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(DEFAULT_LOG_ROUNDS));
    }

    public void verifyPassword(String password, String hash) {
        if(!BCrypt.checkpw(password, hash)){

            log.warn(ERROR_MESSAGE);
            throw new IncorrectCredentialsException(ERROR_MESSAGE);
        }
    }
}
