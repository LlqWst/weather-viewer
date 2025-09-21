package dev.lqwd.repository;

import dev.lqwd.entity.Location;
import dev.lqwd.entity.User;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends AbstractRepository<Location, Long> {
    List<Location> findAllByUserId(User user);

    Optional<Location> findByUserAndId(User user, Long id);

    void deleteByUserAndId(User user, Long id);
}
