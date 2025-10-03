package dev.lqwd.service.weather;

import dev.lqwd.dto.weather.LocationWithStatusResponseDTO;
import dev.lqwd.exception.BadRequestException;
import dev.lqwd.service.repository_service.LocationService;
import dev.lqwd.service.weahter_api.ApiWeatherLocationService;
import dev.lqwd.service.weahter_api.infrastructure.uri_api_builder.UriApiLocationBuilder;
import dev.lqwd.utils.Parser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WeatherLocationService {

    public static final String INCORRECT_SESSION_MESSAGE = "Try to search location with incorrect session ID";
    private final ApiWeatherLocationService apiWeatherLocationService;
    private final UriApiLocationBuilder uriApiLocationBuilder;
    private final LocationService locationService;

    public List<LocationWithStatusResponseDTO> getLocations(String location, String idFromCookie) {
        UUID sessionId = Parser.parseUUID(idFromCookie)
                .orElseThrow(() -> new BadRequestException(INCORRECT_SESSION_MESSAGE));

        String url = uriApiLocationBuilder.build(location);

        Set<LocationKey> existingKeys = locationService.getAllLocations(sessionId).stream()
                .map(loc -> new LocationKey(loc.getLatitude(), loc.getLongitude()))
                .collect(Collectors.toSet());

        return apiWeatherLocationService.fetchApiData(url).stream()
                .map(apiDto -> {
                    LocationKey apiKey = new LocationKey(apiDto.lat(), apiDto.lon());
                    return LocationWithStatusResponseDTO.builder()
                            .location(apiDto)
                            .isAdded(existingKeys.contains(apiKey))
                            .build();
                })
                .toList();
    }

}

record LocationKey(BigDecimal lat, BigDecimal lon) {

    private static final int SCALE = 6;

    LocationKey {
        lat = lat.setScale(SCALE, RoundingMode.HALF_UP);
        lon = lon.setScale(SCALE, RoundingMode.HALF_UP);
    }
}