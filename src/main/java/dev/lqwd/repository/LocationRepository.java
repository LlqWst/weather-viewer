package dev.lqwd.repository;

import dev.lqwd.entity.Location;
import dev.lqwd.entity.User;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends AbstractRepository<Location, Long> {
    public List<Location> findAllByUserId(User user);
    public Optional<Location> findByUserAndId(User user, Long id);
    public void deleteByUserAndId(User user, Long id);
}
