package dev.lqwd.service.weahter_api;

import dev.lqwd.dto.weather.api_response.ApiLocationResponseDTO;
import dev.lqwd.service.ApiHttpClient;
import dev.lqwd.service.JsonSerialization;
import org.springframework.stereotype.Service;

@Service
public class ApiWeatherLocationServiceImpl extends AbstractApiServiceImpl<ApiLocationResponseDTO> implements ApiWeatherLocationService {

    public ApiWeatherLocationServiceImpl(JsonSerialization jsonSerialization, ApiHttpClient apiHttpClient) {
        super(ApiLocationResponseDTO.class, jsonSerialization, apiHttpClient);
    }
}
