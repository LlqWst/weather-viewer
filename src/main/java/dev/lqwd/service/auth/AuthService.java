package dev.lqwd.service.auth;

import dev.lqwd.utils.Validator;
import dev.lqwd.dto.auth.AuthRequestDTO;
import dev.lqwd.entity.Session;
import dev.lqwd.entity.User;
import dev.lqwd.exception.DataBaseException;
import dev.lqwd.repository.SessionRepository;
import dev.lqwd.service.db.SessionService;
import dev.lqwd.service.db.UserService;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class AuthService {

    private static final int SESSION_VALIDATION_INTERVAL_MS = 10 * 60 * 1000;

    private final SessionService sessionService;
    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final CookieService cookieService;

    public AuthService(SessionService sessionService,
                       SessionRepository sessionRepository,
                       UserService userService,
                       CookieService cookieService) {

        this.sessionService = sessionService;
        this.sessionRepository = sessionRepository;
        this.userService = userService;
        this.cookieService = cookieService;
    }

    public Cookie createNewSession(AuthRequestDTO authRequest){

        User user = userService.readByLogin(authRequest);
        String sessionId = sessionService.create(user);
        return cookieService.create(sessionId);
    }

    public boolean isExpired(UUID sessionID) {

        LocalDateTime expiresAt = sessionRepository.findById(sessionID)
                .map(Session::getExpiresAt)
                .orElseThrow(() -> new DataBaseException("Session not found with id: " + sessionID));

        return LocalDateTime.now().isAfter(expiresAt);
    }

    @Scheduled(fixedRate = SESSION_VALIDATION_INTERVAL_MS)
    public int cleanupExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = sessionRepository.deleteByExpiresAtBefore(now);

        log.info("Deleted {} expired sessions", deletedCount);
        return deletedCount;
    }

    public boolean hasValidSession(String sessionId) {
        return Validator.parseUUID(sessionId)
                .filter(sessionService::isPresent)
                .isPresent();
    }

    public Cookie closeSession(String sessionId){
        Validator.parseUUID(sessionId)
                .ifPresentOrElse(
                        sessionService::delete,
                        ()-> log.warn("Attempt to logout with invalid session cookie: {}", sessionId));

        return cookieService.delete();
    }

}
