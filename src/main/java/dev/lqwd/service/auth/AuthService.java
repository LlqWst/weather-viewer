package dev.lqwd.service.auth;

import dev.lqwd.dto.auth.UserRegistrationRequestDTO;
import dev.lqwd.utils.Validator;
import dev.lqwd.dto.auth.AuthRequestDTO;
import dev.lqwd.entity.User;
import dev.lqwd.repository.SessionRepository;
import dev.lqwd.service.repository_service.SessionService;
import dev.lqwd.service.repository_service.UserService;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class AuthService {

    private static final int SESSION_VALIDATION_INTERVAL_MS = 10 * 60 * 1000;

    private final SessionService sessionService;
    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final CookieService cookieService;

    public Cookie openSession(AuthRequestDTO authRequest) {
        User user = userService.readByLogin(authRequest);
        String sessionId = sessionService.create(user);
        return cookieService.create(sessionId);
    }

    @Scheduled(fixedRate = SESSION_VALIDATION_INTERVAL_MS)
    public int cleanupExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = sessionRepository.deleteByExpiresAtBefore(now);

        log.info("Deleted {} expired sessions", deletedCount);
        return deletedCount;
    }

    public Cookie closeSession(String sessionId) {
        Validator.parseUUID(sessionId).ifPresentOrElse(
                sessionService::delete,
                () -> log.warn("Attempt to logout with invalid session cookie: {}", sessionId));

        return cookieService.delete();
    }

    public boolean hasValidSession(String sessionId) {
        if (sessionId == null) {
            return false;
        }

        return Validator.parseUUID(sessionId)
                .map(sessionService::isPresent)
                .orElseGet(() -> {
                    log.warn("Provided incorrect cookie: {}", sessionId);
                    return false;
                });
    }

    public Cookie registration(UserRegistrationRequestDTO registrationRequest) {
        User user = userService.save(registrationRequest);
        String sessionId = sessionService.create(user);
        return cookieService.create(sessionId);
    }

}
