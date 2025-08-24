package dev.lqwd.service;

import dev.lqwd.entity.Session;
import dev.lqwd.entity.User;
import dev.lqwd.exception.DataBaseException;
import dev.lqwd.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
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


    public boolean delete(UUID sessionId){

        if (isPresent(sessionId)) {

            sessionRepository.deleteById(sessionId);
            return true;
        }

        throw new DataBaseException("Error during deletion of session");
    }

}
