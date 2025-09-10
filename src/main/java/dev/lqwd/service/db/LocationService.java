package dev.lqwd.service.db;

import dev.lqwd.utils.Validator;
import dev.lqwd.dto.weather_api.AddLocationRequestDTO;
import dev.lqwd.entity.Location;
import dev.lqwd.entity.User;
import dev.lqwd.exception.BadRequestException;
import dev.lqwd.repository.LocationRepository;
import dev.lqwd.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class LocationService {

    private static final String ERROR_MESSAGE_LOCATION_ADDED = "Location already added for user: {}";
    private final LocationRepository locationRepository;
    private final SessionRepository sessionRepository;

    public LocationService(LocationRepository locationRepository,
                           SessionRepository sessionRepository) {

        this.locationRepository = locationRepository;
        this.sessionRepository = sessionRepository;
    }

    public void save(AddLocationRequestDTO locationDTO, String uuidFromCookie) {
        User user = getUser(uuidFromCookie);
        try {
            locationRepository.save(Location.builder()
                    .user(user)
                    .name(locationDTO.getName())
                    .latitude(locationDTO.getLat())
                    .longitude(locationDTO.getLon())
                    .build());
        }  catch (ConstraintViolationException e) {
            if(e.getKind() == ConstraintViolationException.ConstraintKind.UNIQUE) {
                log.warn(ERROR_MESSAGE_LOCATION_ADDED, user);
                return;
            }
            throw e;
        }
    }

    public List<Location> get(String uuidFromCookie) {
        User user = getUser(uuidFromCookie);
        return locationRepository.findAllByUserId(user);
    }

    public void delete(String uuidFromCookie, String id) {
        try {
            long parsedId = Long.parseLong(id);
            locationRepository.deleteByUserAndId(getUser(uuidFromCookie), parsedId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Incorrect id for deletion: " + id);
        }

    }

    private User getUser(String uuidFromCookie) {
        return Validator.parseUUID(uuidFromCookie)
                .flatMap(sessionRepository::findUserById)
                .orElseThrow(() -> new BadRequestException("Error during find User by UUID: " + uuidFromCookie));
    }
}
