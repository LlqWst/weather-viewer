package dev.lqwd.service;

import dev.lqwd.entity.Session;
import dev.lqwd.entity.User;
import dev.lqwd.exception.DataBaseException;
import dev.lqwd.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class SessionService {

    private static final int SESSION_VALIDATION_INTERVAL_MS = 10 * 60 * 1000;
    private static final String ERROR_MESSAGE_CREATE_SESSION = "Error during save session";

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public String create(User user) {

        return Optional.of(sessionRepository.save(Session.builder()
                                .user(user)
                                .build())
                                .getId()
                                .toString())
                .orElseThrow(() -> new DataBaseException(ERROR_MESSAGE_CREATE_SESSION));

    }

    public boolean isPresent(UUID sessionId) {

        return Optional.ofNullable(sessionId)
                .flatMap(sessionRepository::findById)
                .isPresent();
    }


    public void delete(UUID sessionId) {

        if (isPresent(sessionId)) {
            sessionRepository.deleteById(sessionId);
        } else {
            log.warn("Attempt to logout with invalid session: {}", sessionId);
        }
    }

    public boolean isExpired(UUID sessionID) {

        LocalDateTime expiresAt = sessionRepository.findById(sessionID)
                .map(Session::getExpiresAt)
                .orElseThrow(() -> new DataBaseException("Session not found with id: " + sessionID));

        return LocalDateTime.now().isAfter(expiresAt);
    }

    @Scheduled(fixedRate = SESSION_VALIDATION_INTERVAL_MS)
    public void cleanupExpiredSessions() {

        LocalDateTime now = LocalDateTime.now();
        int deletedCount = sessionRepository.deleteByExpiresAtBefore(now);

        log.info("Deleted {} expired sessions", deletedCount);
    }

}
