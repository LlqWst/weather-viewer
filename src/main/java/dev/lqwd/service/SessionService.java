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

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {

        this.sessionRepository = sessionRepository;
    }

    public String create (User user){

        Session session = Optional.of(
                sessionRepository.save(Session.builder()
                .user(user)
                .build()))
                .orElseThrow(() -> new DataBaseException("Error during save user"));

        return session.getId().toString();
    }

    public boolean isPresent(UUID sessionId){

        if (sessionId == null){
            return false;
        }

        return sessionRepository.findById(sessionId).isPresent();
    }


    public void delete(UUID sessionId){

        if (isPresent(sessionId)) {

            sessionRepository.deleteById(sessionId);
        }

        throw new DataBaseException("Error during deletion of session");
    }

    public boolean isExpired(UUID sessionID){

         LocalDateTime expiresAt = sessionRepository.findById(sessionID)
                .map(Session::getExpiresAt)
                .orElseThrow(() -> new DataBaseException("Session not found with id: " + sessionID));

         return LocalDateTime.now().isAfter(expiresAt);
    }

    @Scheduled(fixedRate = 10 * 30 * 1000)
    public void cleanupExpiredSessions() {

        LocalDateTime now = LocalDateTime.now();
        int deletedCount = sessionRepository.deleteByExpiresAtBefore(now);

        log.info("Deleted {} expired sessions", deletedCount);
    }

}
