package dev.lqwd.service.weahter_api;

import dev.lqwd.dto.ApiLocationsResponseDTO;
import dev.lqwd.dto.ApiWeatherResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeatherApiService {

    private final LocationService locationService;
    private final WeatherService weatherService;

    public WeatherApiService(LocationService locationService, WeatherService weatherService) {
        this.locationService = locationService;
        this.weatherService = weatherService;
    }

//    public Optional<ApiWeatherResponseDTO> getLocationsWithWeather(String location){
//        List<ApiLocationsResponseDTO> locationsDTOs = locationService.getLocations(location);
//        return weatherService.getWeather();
//    }

}
