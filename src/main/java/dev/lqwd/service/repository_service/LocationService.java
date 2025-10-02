package dev.lqwd.service.repository_service;

import dev.lqwd.dto.weather.AddLocationRequestDTO;
import dev.lqwd.entity.Location;
import dev.lqwd.entity.User;
import dev.lqwd.exception.LocationAlreadyAddedException;
import dev.lqwd.exception.UnauthorizedException;
import dev.lqwd.mapper.LocationMapper;
import dev.lqwd.repository.LocationRepository;
import dev.lqwd.utils.Parser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@Slf4j
@AllArgsConstructor
public class LocationService {

    private static final String ERROR_MESSAGE_LOCATION_ALREADY_ADDED = "Location '%s' already added, latitude:%s, longitude:%s";
    private static final String ERROR_MESSAGE_INCORRECT_SESSION_ID = "Incorrect Session Id: ";
    private static final String ERROR_MESSAGE_SESSION_ID_NOT_FOUND = "Error during find User by UUID: ";
    private static final String ERROR_MESSAGE_INCORRECT_LOCATION_ID = "Incorrect location ID for deletion: ";

    private final LocationRepository locationRepository;
    private final SessionService sessionService;
    private final LocationMapper locationMapper;


    public void save(AddLocationRequestDTO locationDTO, String uuidFromCookie) {
        User user = getUserByUuidFromCookie(uuidFromCookie);

        try {
            locationRepository.save(locationMapper.toLocation(locationDTO, user));
        } catch (ConstraintViolationException e) {
            if (e.getKind() == ConstraintViolationException.ConstraintKind.UNIQUE) {
                log.warn("Location already added for user: {}", user);
                throw new LocationAlreadyAddedException(ERROR_MESSAGE_LOCATION_ALREADY_ADDED
                        .formatted(locationDTO.name(), locationDTO.lat(), locationDTO.lon()));
            }
            throw e;
        }
    }

    public List<Location> get(UUID sessionId) {
        return locationRepository.findAllByUserId(sessionId);
    }

    public void delete(String uuidFromCookie, String locationId) {
        UUID sessionId = getSessionId(uuidFromCookie);
        long parsedLocationId = Parser.parseLocationId(locationId)
                .orElseThrow(() -> new UnauthorizedException(ERROR_MESSAGE_INCORRECT_LOCATION_ID + locationId));

        locationRepository.deleteByUserIdAndLocationId(sessionId, parsedLocationId);
    }

    private static UUID getSessionId(String uuidFromCookie) {
        return Parser.parseUUID(uuidFromCookie)
                .orElseThrow(() -> new UnauthorizedException(ERROR_MESSAGE_INCORRECT_SESSION_ID + uuidFromCookie));
    }

    private User getUserByUuidFromCookie(String uuidFromCookie) {
        UUID sessionId = getSessionId(uuidFromCookie);
        return sessionService.getUserById(sessionId)
                .orElseThrow(() -> new UnauthorizedException(ERROR_MESSAGE_SESSION_ID_NOT_FOUND + uuidFromCookie));

    }
}
