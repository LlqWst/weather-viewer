package dev.lqwd.repository;

import dev.lqwd.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public class UserRepositoryImpl extends AbstractRepository<User, Long> implements UserRepository {

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(
                entityManager.createNamedQuery("User.findByLogin", User.class)
                        .setParameter("login", login)
                        .getSingleResultOrNull());
    }

}


