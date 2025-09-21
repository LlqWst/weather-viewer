package dev.lqwd.service.weahter_api;

import dev.lqwd.dto.weather.api_response.ApiCurrentWeatherResponseDTO;
import dev.lqwd.service.weahter_api.infrastructure.ExternalApiExceptionHandler;
import dev.lqwd.service.weahter_api.infrastructure.ApiHttpClient;
import dev.lqwd.service.weahter_api.infrastructure.JsonResponseParser;
import org.springframework.stereotype.Service;


@Service
public class ApiCurrentWeatherServiceImpl extends AbstractApiServiceImpl<ApiCurrentWeatherResponseDTO> implements ApiCurrentWeatherService {

    public ApiCurrentWeatherServiceImpl(JsonResponseParser jsonResponseParser,
                                        ApiHttpClient apiHttpClient,
                                        ExternalApiExceptionHandler externalApiExceptionHandler) {

        super(ApiCurrentWeatherResponseDTO.class,
                jsonResponseParser,
                apiHttpClient,
                externalApiExceptionHandler);
    }

}
