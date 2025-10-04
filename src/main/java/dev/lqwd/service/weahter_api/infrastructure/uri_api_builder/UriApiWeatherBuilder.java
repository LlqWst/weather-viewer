package dev.lqwd.service.weahter_api.infrastructure.uri_api_builder;

import dev.lqwd.configuration.app_config.HttpClientConfig;
import dev.lqwd.entity.Location;
import dev.lqwd.exception.BadRequestException;
import dev.lqwd.utils.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@AllArgsConstructor
public class UriApiWeatherBuilder {

    private static final String URL_WEATHER = "https://api.openweathermap.org/data/2.5/weather";
    private static final String UNITS_CELSIUS = "metric";
    private final HttpClientConfig httpClientConfig;

    public String build(Location location) throws BadRequestException {
        String url = UriComponentsBuilder.fromUriString(URL_WEATHER)
                .queryParam("lat", location.getLatitude())
                .queryParam("lon", location.getLongitude())
                .queryParam("appid", httpClientConfig.getAppId())
                .queryParam("units", UNITS_CELSIUS)
                .encode()
                .build()
                .toUriString();

        Validator.validateUrlLength(url.length());
        return url;
    }
}
