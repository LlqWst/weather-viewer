package dev.lqwd.service;

import dev.lqwd.dto.weather_api.api_response.ApiCurrentWeatherResponseDTO;
import dev.lqwd.dto.weather_api.CurrentWeatherResponseDTO;
import dev.lqwd.entity.Location;
import dev.lqwd.mapper.CurrentWeatherMapper;
import dev.lqwd.service.repository_service.LocationService;
import dev.lqwd.service.weahter_api.CurrentWeatherApiService;
import dev.lqwd.uri_builder.UriApiWeatherBuilder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class WeatherService {

    private final CurrentWeatherMapper weatherOfLocationMapper = CurrentWeatherMapper.INSTANCE;
    private final LocationService locationService;
    private final CurrentWeatherApiService currentWeatherApiService;

    public WeatherService(LocationService locationService,
                          CurrentWeatherApiService currentWeatherApiService) {

        this.locationService = locationService;
        this.currentWeatherApiService = currentWeatherApiService;
    }

    public List<CurrentWeatherResponseDTO> getWeatherForUser(String sessionId) {
        List<Location> locations = locationService.get(sessionId);
        if (locations.isEmpty()) {
            return Collections.emptyList();
        }
        return getCurrentWeather(locations);
    }

    private List<CurrentWeatherResponseDTO> getCurrentWeather(List<Location> locations) {
        return locations.stream().map(location -> {
                    String url = new UriApiWeatherBuilder(location).build();
                    ApiCurrentWeatherResponseDTO weatherData = currentWeatherApiService.fetchApiData(url).get(0);
                    CurrentWeatherResponseDTO responseDto = weatherOfLocationMapper.toResponseDto(weatherData);
                    responseDto.setId(location.getId());
                    responseDto.setName(location.getName());
                    return responseDto;
        })
                .toList();
    }
}
