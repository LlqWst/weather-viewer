package dev.lqwd.service.weahter_api;

import dev.lqwd.dto.weather.api_response.ApiLocationResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class ApiWeatherLocationServiceImpl extends AbstractApiServiceImpl<ApiLocationResponseDTO> implements ApiWeatherLocationService {

    public ApiWeatherLocationServiceImpl() {
        super(ApiLocationResponseDTO.class);
    }

}
