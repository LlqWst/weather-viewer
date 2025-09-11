package dev.lqwd.service.weahter_api;

import dev.lqwd.dto.weather_api.api_response.ApiCurrentWeatherResponseDTO;
import org.springframework.stereotype.Service;


@Service
public class CurrentWeatherApiServiceImpl extends AbstractApiServiceImpl<ApiCurrentWeatherResponseDTO> implements CurrentWeatherApiService {

    public CurrentWeatherApiServiceImpl() {
        super(ApiCurrentWeatherResponseDTO.class);
    }
}
