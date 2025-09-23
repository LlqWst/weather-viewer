package dev.lqwd.service.weahter_api;

import dev.lqwd.dto.weather.api_response.ApiLocationResponseDTO;
import dev.lqwd.service.weahter_api.infrastructure.ExternalApiExceptionHandler;
import dev.lqwd.service.weahter_api.infrastructure.ApiHttpClient;
import dev.lqwd.service.weahter_api.infrastructure.JsonDeserializer;
import org.springframework.stereotype.Service;

@Service
public class ApiWeatherLocationServiceImpl extends AbstractApiServiceImpl<ApiLocationResponseDTO> implements ApiWeatherLocationService {

    public ApiWeatherLocationServiceImpl(JsonDeserializer jsonDeserializer,
                                         ApiHttpClient apiHttpClient,
                                         ExternalApiExceptionHandler externalApiExceptionHandler) {

        super(ApiLocationResponseDTO.class,
                jsonDeserializer,
                apiHttpClient,
                externalApiExceptionHandler);
    }
}
