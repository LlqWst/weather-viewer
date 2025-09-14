package dev.lqwd.service.weather;

import dev.lqwd.dto.weather.api_response.ApiCurrentWeatherResponseDTO;
import dev.lqwd.dto.weather.CurrentWeatherResponseDTO;
import dev.lqwd.entity.Location;
import dev.lqwd.mapper.CurrentWeatherMapper;
import dev.lqwd.service.repository_service.LocationService;
import dev.lqwd.service.weahter_api.ApiCurrentWeatherService;
import dev.lqwd.uri_builder.UriApiWeatherBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CurrentWeatherService {

    private final CurrentWeatherMapper currentWeatherMapper = CurrentWeatherMapper.INSTANCE;
    private final LocationService locationService;
    private final ApiCurrentWeatherService apiCurrentWeatherService;


    public List<CurrentWeatherResponseDTO> getWeatherForUser(String sessionId) {
        List<Location> locations = locationService.get(sessionId);
        if (locations.isEmpty()) {
            return Collections.emptyList();
        }
        return getCurrentWeather(locations);
    }

    private List<CurrentWeatherResponseDTO> getCurrentWeather(List<Location> locations) {
        return locations.stream()
                .map(location -> {
                    String url = new UriApiWeatherBuilder(location).build();
                    ApiCurrentWeatherResponseDTO weatherData = apiCurrentWeatherService.findFirst(url);
                    CurrentWeatherResponseDTO responseDto = currentWeatherMapper.toResponseDto(weatherData);
                    responseDto.setId(location.getId());
                    responseDto.setName(location.getName());
                    return responseDto;
                })
                .toList();
    }
}
