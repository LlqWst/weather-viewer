package dev.lqwd.service.repository_service;

import dev.lqwd.dto.weather.AddLocationRequestDTO;
import dev.lqwd.entity.Location;
import dev.lqwd.entity.User;
import dev.lqwd.exception.BadRequestException;
import dev.lqwd.exception.UnauthorizedException;
import dev.lqwd.repository.LocationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;

import java.util.List;


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
        User user = getUser(uuidFromCookie);
        try {
            locationRepository.save(Location.builder()
                    .user(user)
                    .name(locationDTO.getName())
                    .latitude(locationDTO.getLat())
                    .longitude(locationDTO.getLon())
                    .build());
        } catch (ConstraintViolationException e) {
            if (e.getKind() == ConstraintViolationException.ConstraintKind.UNIQUE) {
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
            User user = getUser(uuidFromCookie);
            locationRepository.deleteByUserAndId(user, parsedId);
        } catch (NumberFormatException e) {
            throw new UnauthorizedException(ERROR_MESSAGE_DELETION_USER + id);
        }
    }

    private User getUser(String uuidFromCookie) {
        return sessionService.getUserById(uuidFromCookie)
                .orElseThrow(() -> new UnauthorizedException(ERROR_MESSAGE_FINDING_USER + uuidFromCookie));
    }
}
