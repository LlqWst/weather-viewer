package dev.lqwd.repository;

import dev.lqwd.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserRep {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(User user){
        entityManager.persist(user);
    }

    @Transactional(readOnly = true)
    public User find(int id){
        return entityManager.find(User.class, id);
    }

}
