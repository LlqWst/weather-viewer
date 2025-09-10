package dev.lqwd.service.weahter_api;


import dev.lqwd.dto.weather_api.ApiCurrentWeatherDTO;
import org.springframework.stereotype.Service;


@Service
public class WeatherApiServiceImpl extends AbstractApiServiceImpl<ApiCurrentWeatherDTO> implements WeatherApiService {

    public WeatherApiServiceImpl() {
        super(ApiCurrentWeatherDTO.class);
    }
}
