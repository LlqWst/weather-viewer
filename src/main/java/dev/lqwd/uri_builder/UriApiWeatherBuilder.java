package dev.lqwd.uri_builder;

import dev.lqwd.configuration.UriConfig;
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
    private static final String UNITS = "metric";
    private final UriConfig uriConfig;

    public String build(Location location) throws BadRequestException {
        String url = UriComponentsBuilder.fromUriString(URL_WEATHER)
                .queryParam("lat", location.getLatitude())
                .queryParam("lon", location.getLongitude())
                .queryParam("appid", uriConfig.getAppId())
                .queryParam("units", UNITS)
                .build()
                .toUriString();

        Validator.validateUrlLength(url.length());
        return url;
    }
}
