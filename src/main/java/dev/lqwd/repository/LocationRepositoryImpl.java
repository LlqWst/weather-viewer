package dev.lqwd.repository;

import dev.lqwd.entity.Location;
import dev.lqwd.entity.User;
import dev.lqwd.exception.BadRequestException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


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

    @Override
    @Transactional(readOnly = true)
    public Optional<Location> findByUserAndId(User user, Long id) {
        return Optional.ofNullable(entityManager.createQuery(
                        "SELECT l FROM Location AS l WHERE l.user = :user AND l.id = :id", Location.class)
                .setParameter("user", user)
                .setParameter("id", id)
                .getSingleResultOrNull());
    }

    @Override
    @Transactional
    public void deleteByUserAndId(User user, Long id) {
        Location location = findByUserAndId(user, id)
                .orElseThrow(() -> new BadRequestException("Location with id %d not found for user %s".formatted(id, user)));

        entityManager.remove(location);
    }

}


