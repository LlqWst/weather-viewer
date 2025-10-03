package dev.lqwd.repository;

import dev.lqwd.entity.Location;
import dev.lqwd.exception.DataBaseException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class LocationRepositoryImpl extends AbstractRepositoryImpl<Location, Long> implements LocationRepository {

    public static final String INCORRECT_LOCATION_ID_MESSAGE = "Location with id %d not found for user %s";

    public LocationRepositoryImpl() {
        super(Location.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Location> findAllByUserId(UUID sessionId) {
        return entityManager.createQuery("""
                        SELECT l
                        FROM Location AS l
                        JOIN Session AS s ON l.user = s.user
                        WHERE s.id = :sessionId
                        """, Location.class)
                .setParameter("sessionId", sessionId)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Location> findByUserIdAndLocationId(UUID sessionId, Long id) {
        return Optional.ofNullable(entityManager.createQuery("""
                        SELECT l
                        FROM Location AS l
                        JOIN Session AS s ON l.user = s.user
                        WHERE s.id = :sessionId
                        AND l.id = :id""", Location.class)
                .setParameter("sessionId", sessionId)
                .setParameter("id", id)
                .getSingleResultOrNull());
    }

    @Override
    @Transactional
    public void deleteByUserIdAndLocationId(UUID sessionId, Long id) {
        Location location = findByUserIdAndLocationId(sessionId, id)
                .orElseThrow(() -> new DataBaseException(INCORRECT_LOCATION_ID_MESSAGE.formatted(id, sessionId)));
        entityManager.remove(location);
    }

}


