package dev.lqwd.repository;

import dev.lqwd.entity.Location;
import dev.lqwd.entity.User;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends AbstractRepository<Location, Long> {
    public List<Location> findAllByUserId(User user);
}
