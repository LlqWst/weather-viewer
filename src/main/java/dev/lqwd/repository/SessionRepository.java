package dev.lqwd.repository;

import dev.lqwd.entity.Session;
import dev.lqwd.entity.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends AbstractRepository<Session, UUID> {
    int deleteByExpiresAtBefore(LocalDateTime now);
    public Optional<User> findUserById(UUID sessionId);
}
