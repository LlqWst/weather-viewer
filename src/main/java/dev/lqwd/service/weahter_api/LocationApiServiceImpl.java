package dev.lqwd.service.weahter_api;

import dev.lqwd.dto.weather_api.api_response.ApiLocationResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class LocationApiServiceImpl extends AbstractApiServiceImpl<ApiLocationResponseDTO> implements LocationApiService {

    public LocationApiServiceImpl() {
        super(ApiLocationResponseDTO.class);
    }

}
