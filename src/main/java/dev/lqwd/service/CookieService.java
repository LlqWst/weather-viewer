package dev.lqwd.service;

import dev.lqwd.exception.DataBaseException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CookieService {

    private static final int COOKIE_AGE = 60 * 30;
    private static final String SESSION_ID = "sessionId";
    private static final String EMPTY = "";
    private static final String ERROR_MESSAGE_INCORRECT_SESSION_ID = "incorrect data type for session id: %s";

    public Cookie create(String sessionId) {

        Cookie cookie = new Cookie(SESSION_ID, sessionId);
        cookie.setMaxAge(COOKIE_AGE);

        return cookie;
    }

    public Cookie delete() {

        Cookie cookie = new Cookie(SESSION_ID, EMPTY);
        cookie.setMaxAge(0);

        return cookie;
    }

    public Optional<UUID> getSessionId(HttpServletRequest request) {

        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> SESSION_ID.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .map(this::parseUUID);
    }

    private UUID parseUUID(String id) {
        try {
            return UUID.fromString(id);

        } catch (IllegalArgumentException e) {
            throw new DataBaseException(ERROR_MESSAGE_INCORRECT_SESSION_ID.formatted(id));
        }
    }

}
