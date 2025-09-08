package dev.lqwd.repository;

import dev.lqwd.entity.Session;
import dev.lqwd.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class SessionRepositoryImpl extends AbstractRepositoryImpl<Session, UUID> implements SessionRepository {

    public SessionRepositoryImpl() {
        super(Session.class);
    }

    @Override
    @Transactional
    public int deleteByExpiresAtBefore(LocalDateTime now) {

        return entityManager.createQuery("DELETE FROM Session AS s WHERE s.expiresAt < :now")
                .setParameter("now", now)
                .executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserById(UUID sessionId) {

        return Optional.ofNullable(entityManager.createQuery("SELECT s.user FROM Session AS s WHERE s.id = :sessionId", User.class)
                .setParameter("sessionId", sessionId)
                .getSingleResultOrNull());
    }
}


