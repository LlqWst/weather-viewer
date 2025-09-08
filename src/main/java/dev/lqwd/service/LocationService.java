package dev.lqwd.service;

import dev.lqwd.Validator;
import dev.lqwd.dto.weather_api.AddLocationRequestDTO;
import dev.lqwd.entity.Location;
import dev.lqwd.entity.User;
import dev.lqwd.exception.BadRequestException;
import dev.lqwd.exception.DataBaseException;
import dev.lqwd.repository.LocationRepository;
import dev.lqwd.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final SessionRepository sessionRepository;

    public LocationService(LocationRepository locationRepository,
                           SessionRepository sessionRepository) {

        this.locationRepository = locationRepository;
        this.sessionRepository = sessionRepository;
    }

    public void save(AddLocationRequestDTO locationDTO, String uuidFromCookie) {

        User user = Validator.parseUUID(uuidFromCookie)
                .flatMap(sessionRepository::findUserById)
                .orElseThrow(() -> new DataBaseException("Error during find User by UUID: " + uuidFromCookie));

        locationRepository.save(Location.builder()
                .user(user)
                .name(locationDTO.getName())
                .latitude(locationDTO.getLat())
                .longitude(locationDTO.getLon())
                .build());

    }
}
