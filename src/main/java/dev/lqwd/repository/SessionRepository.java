package dev.lqwd.repository;

import dev.lqwd.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface SessionRepository extends JpaRepository<Session, UUID> {

}
