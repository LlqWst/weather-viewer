package dev.lqwd.service.repository_service;

import dev.lqwd.dto.weather.AddLocationRequestDTO;
import dev.lqwd.entity.Location;
import dev.lqwd.entity.User;
import dev.lqwd.exception.UnauthorizedException;
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

    private static final String ERROR_MESSAGE_LOCATION_ADDED = "Location already added for user: {}";
    private static final String ERROR_MESSAGE_FINDING_USER = "Error during find User by UUID: ";
    private static final String ERROR_MESSAGE_DELETION_USER = "Incorrect ID for deletion: ";

    private final LocationRepository locationRepository;
    private final SessionService sessionService;


    public void save(AddLocationRequestDTO locationDTO, String uuidFromCookie) {
        User user = getUserByCookie(uuidFromCookie);
        try {
            locationRepository.save(Location.builder()
                    .user(user)
                    .name(locationDTO.name())
                    .latitude(locationDTO.lat())
                    .longitude(locationDTO.lon())
                    .build());
        } catch (ConstraintViolationException e) {
            if (e.getKind() == ConstraintViolationException.ConstraintKind.UNIQUE) {
                log.warn(ERROR_MESSAGE_LOCATION_ADDED, user);
                return;
            }
            throw e;
        }
    }

    public List<Location> get(UUID sessionId) {
        User user = getUserById(sessionId);
        return locationRepository.findAllByUserId(user);
    }

    public void delete(String uuidFromCookie, String locationId) {
        User user = getUserByCookie(uuidFromCookie);
        long parsedId = Parser.parseLocationId(locationId)
                .orElseThrow(() -> new UnauthorizedException(ERROR_MESSAGE_DELETION_USER + locationId));

        locationRepository.deleteByUserAndId(user, parsedId);
    }

    private User getUserByCookie(String uuidFromCookie) {
        return Parser.parseUUID(uuidFromCookie)
                .map(this::getUserById)
                .orElseThrow();
    }

    private User getUserById(UUID sessionId) {
        return sessionService.getUserById(sessionId)
                .orElseThrow(() -> new UnauthorizedException(ERROR_MESSAGE_FINDING_USER + sessionId));
    }
}
