package dev.lqwd.repository;

import dev.lqwd.entity.Location;
import org.springframework.stereotype.Repository;


@Repository
public class LocationRepositoryImpl extends AbstractRepositoryImpl<Location, Long> implements LocationRepository {

    public LocationRepositoryImpl() {
        super(Location.class);
    }

}


