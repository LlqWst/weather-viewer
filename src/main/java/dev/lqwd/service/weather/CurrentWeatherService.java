package dev.lqwd.service.weather;

import dev.lqwd.dto.weather.api_response.ApiCurrentWeatherResponseDTO;
import dev.lqwd.dto.weather.CurrentWeatherResponseDTO;
import dev.lqwd.entity.Location;
import dev.lqwd.mapper.CurrentWeatherMapper;
import dev.lqwd.service.repository_service.LocationService;
import dev.lqwd.service.weahter_api.ApiCurrentWeatherService;
import dev.lqwd.uri_builder.UriApiWeatherBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CurrentWeatherService {

    private final CurrentWeatherMapper currentWeatherMapper;
    private final LocationService locationService;
    private final ApiCurrentWeatherService apiCurrentWeatherService;
    private final UriApiWeatherBuilder uriApiWeatherBuilder;


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
                    String url = uriApiWeatherBuilder.build(location);
                    List<ApiCurrentWeatherResponseDTO> weatherData = apiCurrentWeatherService.fetchApiData(url);
                    if (weatherData.isEmpty()) {
                        return getEmptyWeatherDTO(weatherData);
                    }
                    CurrentWeatherResponseDTO responseDto = currentWeatherMapper.toResponseDto(weatherData.get(0));
                    responseDto.setId(location.getId());
                    responseDto.setName(location.getName());
                    return responseDto;
                })
                .toList();
    }

    private CurrentWeatherResponseDTO getEmptyWeatherDTO(List<ApiCurrentWeatherResponseDTO> weatherData) {
        log.warn("Api return empty list for location: {}", weatherData);
        return new CurrentWeatherResponseDTO();
    }
}
