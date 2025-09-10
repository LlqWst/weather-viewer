package dev.lqwd.service.auth;

import dev.lqwd.utils.Validator;
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

    public Cookie create(String sessionId) {
        Cookie cookie = new Cookie(SESSION_ID, sessionId);
        cookie.setMaxAge(COOKIE_AGE_MINUTES);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    public Cookie delete() {
        Cookie cookie = new Cookie(SESSION_ID, EMPTY);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        return cookie;
    }

    public Optional<UUID> getSessionId(Cookie[] cookies) {

        return Optional.ofNullable(cookies)
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> SESSION_ID.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .flatMap(Validator::parseUUID);
    }

}
