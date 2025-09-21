package dev.lqwd.service.weahter_api;

import dev.lqwd.dto.weather.api_response.ApiLocationResponseDTO;
import dev.lqwd.service.weahter_api.infrastructure.ExternalApiExceptionHandler;
import dev.lqwd.service.weahter_api.infrastructure.ApiHttpClient;
import dev.lqwd.service.weahter_api.infrastructure.JsonResponseParser;
import org.springframework.stereotype.Service;

@Service
public class ApiWeatherLocationServiceImpl extends AbstractApiServiceImpl<ApiLocationResponseDTO> implements ApiWeatherLocationService {

    public ApiWeatherLocationServiceImpl(JsonResponseParser jsonResponseParser,
                                         ApiHttpClient apiHttpClient,
                                         ExternalApiExceptionHandler externalApiExceptionHandler) {

        super(ApiLocationResponseDTO.class,
                jsonResponseParser,
                apiHttpClient,
                externalApiExceptionHandler);
    }
}
