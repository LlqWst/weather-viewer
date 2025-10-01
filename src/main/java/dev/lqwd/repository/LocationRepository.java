package dev.lqwd.repository;

import dev.lqwd.entity.Location;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationRepository extends AbstractRepository<Location, Long> {
    List<Location> findAllByUserId(UUID sessionId);
    Optional<Location> findByUserIdAndLocationId(UUID sessionId, Long id);
    void deleteByUserIdAndLocationId(UUID sessionId, Long id);
}
