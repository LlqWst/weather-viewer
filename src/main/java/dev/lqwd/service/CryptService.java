package dev.lqwd.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class CryptService {

    private static final int DEFAULT_LOG_ROUNDS = 12;

    public String getHashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(DEFAULT_LOG_ROUNDS));
    }

    public boolean verifyPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
