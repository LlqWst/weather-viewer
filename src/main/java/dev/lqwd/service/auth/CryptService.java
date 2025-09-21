package dev.lqwd.service.auth;

import dev.lqwd.exception.user_validation.UserAuthException;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CryptService {

    private static final int DEFAULT_LOG_ROUNDS = 12;
    private static final String ERROR_MESSAGE_INCORRECT_PASSWORD = "Incorrect Password";

    public String getHashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(DEFAULT_LOG_ROUNDS));
    }

    public void verifyPassword(String password, String hash) {
        if (!BCrypt.checkpw(password, hash)) {
            log.warn(ERROR_MESSAGE_INCORRECT_PASSWORD);
            throw new UserAuthException(ERROR_MESSAGE_INCORRECT_PASSWORD);
        }
    }
}
