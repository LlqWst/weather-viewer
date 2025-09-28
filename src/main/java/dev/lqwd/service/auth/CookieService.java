package dev.lqwd.service.auth;

import dev.lqwd.utils.Parser;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CookieService {

    private static final int COOKIE_AGE_MINUTES = 60 * 30;
    private static final String SESSION_ID = "sessionId";
    private static final String EMPTY = "";
    public static final String ROOT_URI = "/";
    public static final int EXPIRY = 0;

    public Cookie create(String sessionId) {
        Cookie cookie = new Cookie(SESSION_ID, sessionId);
        cookie.setMaxAge(COOKIE_AGE_MINUTES);
        cookie.setPath(ROOT_URI);
        cookie.setHttpOnly(true);

        return cookie;
    }

    public Cookie delete() {
        Cookie cookie = new Cookie(SESSION_ID, EMPTY);
        cookie.setMaxAge(EXPIRY);
        cookie.setPath(ROOT_URI);

        return cookie;
    }

    public Optional<UUID> getSessionId(Cookie[] cookies) {
        return Optional.ofNullable(cookies)
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> SESSION_ID.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .flatMap(Parser::parseUUID);
    }

}
