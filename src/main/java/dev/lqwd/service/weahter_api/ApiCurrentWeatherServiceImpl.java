package dev.lqwd.service.weahter_api;

import dev.lqwd.dto.weather.api_response.ApiCurrentWeatherResponseDTO;
import dev.lqwd.service.weahter_api.infrastructure.ExternalApiExceptionHandler;
import dev.lqwd.service.weahter_api.infrastructure.ApiHttpClient;
import dev.lqwd.service.weahter_api.infrastructure.JsonDeserializer;
import org.springframework.stereotype.Service;


@Service
public class ApiCurrentWeatherServiceImpl extends AbstractApiServiceImpl<ApiCurrentWeatherResponseDTO> implements ApiCurrentWeatherService {

    public ApiCurrentWeatherServiceImpl(JsonDeserializer jsonDeserializer,
                                        ApiHttpClient apiHttpClient,
                                        ExternalApiExceptionHandler externalApiExceptionHandler) {

        super(ApiCurrentWeatherResponseDTO.class,
                jsonDeserializer,
                apiHttpClient,
                externalApiExceptionHandler);
    }

}
