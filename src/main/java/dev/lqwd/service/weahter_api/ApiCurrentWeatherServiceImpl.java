package dev.lqwd.service.weahter_api;

import dev.lqwd.dto.weather.api_response.ApiCurrentWeatherResponseDTO;
import dev.lqwd.service.ApiHttpClient;
import dev.lqwd.service.JsonSerialization;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
public class ApiCurrentWeatherServiceImpl extends AbstractApiServiceImpl<ApiCurrentWeatherResponseDTO> implements ApiCurrentWeatherService {

    public ApiCurrentWeatherServiceImpl(JsonSerialization jsonSerialization, ApiHttpClient apiHttpClient) {
        super(ApiCurrentWeatherResponseDTO.class, jsonSerialization, apiHttpClient);
    }
}
