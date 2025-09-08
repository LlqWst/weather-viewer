package dev.lqwd.service.weahter_api;

import dev.lqwd.dto.weather_api.ApiLocationsResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class LocationApiServiceImpl extends AbstractApiServiceImpl<ApiLocationsResponseDTO> implements LocationApiService {

    public LocationApiServiceImpl() {
        super(ApiLocationsResponseDTO.class);
    }
}
