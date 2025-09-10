package dev.lqwd.service;

import dev.lqwd.utils.Converter;
import dev.lqwd.dto.weather_api.ApiCurrentWeatherDTO;
import dev.lqwd.dto.weather_api.WeatherOfLocationResponseDTO;
import dev.lqwd.entity.Location;
import dev.lqwd.service.db.LocationService;
import dev.lqwd.service.weahter_api.WeatherApiService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class WeatherService {

    private static final String ICON_URL = "https://openweathermap.org/img/wn/%s@2x.png";
    private static final String URL_WEATHER = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s";
    private static final String APP_ID = System.getenv("APP_ID");
    private final LocationService locationService;
    private final WeatherApiService weatherApiService;

    public WeatherService(LocationService locationService,
                          WeatherApiService weatherApiService) {
        this.locationService = locationService;
        this.weatherApiService = weatherApiService;
    }

    public List<WeatherOfLocationResponseDTO> getWeatherForUser(String sessionId) {
        List<Location> locations = locationService.get(sessionId);
        if (locations.isEmpty()) {
            return Collections.emptyList();
        }
        return getCurrentWeather(locations);
    }

    private List<WeatherOfLocationResponseDTO> getCurrentWeather(List<Location> locations) {
        List<WeatherOfLocationResponseDTO> weatherOfLocationResponseDTOS = new ArrayList<>();
        for (Location location : locations) {
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());
            String parameterizedURI = URL_WEATHER.formatted(latitude, longitude, APP_ID);

            ApiCurrentWeatherDTO apiCurrentWeatherDTO = weatherApiService.fetchApiData(parameterizedURI).get(0);
            WeatherOfLocationResponseDTO weatherOfLocationResponseDTO = toResponseDto(apiCurrentWeatherDTO);
            weatherOfLocationResponseDTO.setId(location.getId());
            weatherOfLocationResponseDTOS.add(weatherOfLocationResponseDTO);
        }
        return weatherOfLocationResponseDTOS;
    }

    private WeatherOfLocationResponseDTO toResponseDto(ApiCurrentWeatherDTO currentWeatherDTO){
        return WeatherOfLocationResponseDTO.builder()
                .country(currentWeatherDTO.getSys().getCountry())
                .name(currentWeatherDTO.getName())
                .temp(Converter.convertToCelsius(
                        currentWeatherDTO.getMain().getTemp()))
                .feelsLike(Converter.convertToCelsius(
                        currentWeatherDTO.getMain().getFeelsLike()))
                .humidity(currentWeatherDTO.getMain().getHumidity())
                .description(currentWeatherDTO.getWeather().get(0).getDescription())
                .iconUrl(ICON_URL.formatted(
                        currentWeatherDTO.getWeather().get(0).getIcon()))
                .build();
    }

}
