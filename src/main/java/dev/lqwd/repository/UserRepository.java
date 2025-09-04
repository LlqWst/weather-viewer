package dev.lqwd.repository;

import dev.lqwd.entity.User;

import java.util.Optional;

public interface UserRepository extends AbstractRepository<User, Long> {
    Optional<User> findByLogin(String login);
}