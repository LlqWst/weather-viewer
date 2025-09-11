package dev.lqwd.service.repository_service;

import dev.lqwd.entity.Session;
import dev.lqwd.entity.User;
import dev.lqwd.exception.DataBaseException;
import dev.lqwd.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class SessionService {

    private static final String ERROR_MESSAGE_CREATE_SESSION = "Error during save session for user: ";

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
                .orElseThrow(() -> new DataBaseException(ERROR_MESSAGE_CREATE_SESSION + user));
    }

    public boolean isPresent(UUID sessionId) {
        if (sessionId == null){
            return false;
        }
        return sessionRepository.findById(sessionId)
                .isPresent();
    }


    public void delete(UUID sessionId) {
        if (isPresent(sessionId)) {
            sessionRepository.deleteById(sessionId);
        } else {
            log.warn("Attempt to logout with invalid UUID: {}", sessionId);
        }
    }

}
