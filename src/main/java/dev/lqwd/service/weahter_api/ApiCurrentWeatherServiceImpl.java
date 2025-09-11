package dev.lqwd.service.weahter_api;

import dev.lqwd.dto.weather.api_response.ApiCurrentWeatherResponseDTO;
import org.springframework.stereotype.Service;


@Service
public class ApiCurrentWeatherServiceImpl extends AbstractApiServiceImpl<ApiCurrentWeatherResponseDTO> implements ApiCurrentWeatherService {

    public ApiCurrentWeatherServiceImpl() {
        super(ApiCurrentWeatherResponseDTO.class);
    }
}
