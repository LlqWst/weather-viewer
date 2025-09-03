package dev.lqwd.repository;

import dev.lqwd.entity.Session;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SessionRepository extends RepositoryInterface<Session, UUID> {
    int deleteByExpiresAtBefore(LocalDateTime now);
}
