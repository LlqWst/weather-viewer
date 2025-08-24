package dev.lqwd.repository;

import dev.lqwd.entity.Session;
import dev.lqwd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


public interface SessionRepository extends JpaRepository<Session, UUID> {

    @Modifying
    @Query("DELETE FROM Session AS s WHERE s.expiresAt < :now")
    @Transactional
    int deleteByExpiresAtBefore(@Param("now") LocalDateTime now);
}
