package dev.lqwd.service.weather;

import dev.lqwd.dto.weather.api_response.ApiLocationResponseDTO;
import dev.lqwd.service.weahter_api.ApiWeatherLocationService;
import dev.lqwd.uri_builder.UriApiLocationBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WeatherLocationService {

    private final ApiWeatherLocationService apiWeatherLocationService;
    private final UriApiLocationBuilder uriApiLocationBuilder;

    public List<ApiLocationResponseDTO> getLocations(String location) {
        String url = uriApiLocationBuilder.build(location);
        return apiWeatherLocationService.fetchApiData(url);
    }
}
