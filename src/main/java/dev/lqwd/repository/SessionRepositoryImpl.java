package dev.lqwd.repository;

import dev.lqwd.entity.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class SessionRepositoryImpl extends AbstractRepository<Session, UUID> implements SessionRepository {

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
}


