package dev.lqwd;

import lombok.experimental.UtilityClass;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;


@Component
@UtilityClass
public class BCryptUtil {

    private static final int DEFAULT_LOG_ROUNDS = 12;

    public static String getHashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(DEFAULT_LOG_ROUNDS));
    }

    public static boolean verifyPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
