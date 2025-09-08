package dev.lqwd.service.weahter_api;


import dev.lqwd.dto.weather_api.ApiWeatherResponseDTO;
import org.springframework.stereotype.Service;


@Service
public class WeatherApiServiceImpl extends AbstractApiServiceImpl<ApiWeatherResponseDTO> implements WeatherApiService {

    public WeatherApiServiceImpl() {
        super(ApiWeatherResponseDTO.class);
    }
}
