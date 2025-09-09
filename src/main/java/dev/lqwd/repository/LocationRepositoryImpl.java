package dev.lqwd.repository;

import dev.lqwd.entity.Location;
import dev.lqwd.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Repository
public class LocationRepositoryImpl extends AbstractRepositoryImpl<Location, Long> implements LocationRepository {

    public LocationRepositoryImpl() {
        super(Location.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Location> findAllByUserId(User user) {
        return entityManager.createQuery("SELECT l FROM Location AS l WHERE l.user = :user", Location.class)
                .setParameter("user", user)
                .getResultList();
    }

}


