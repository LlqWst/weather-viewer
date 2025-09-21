package dev.lqwd.uri_builder;

import dev.lqwd.configuration.UriConfig;
import dev.lqwd.exception.BadRequestException;
import dev.lqwd.utils.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@AllArgsConstructor
public class UriApiLocationBuilder {

    private static final String URL_LOCATIONS = "https://api.openweathermap.org/geo/1.0/direct";
    private static final int MAX_LIMIT = 5;
    private final UriConfig uriConfig;

    public String build(String location) throws BadRequestException {
        String url = UriComponentsBuilder.fromUriString(URL_LOCATIONS)
                .queryParam("q", location)
                .queryParam("limit", MAX_LIMIT)
                .queryParam("appid", uriConfig.getAppId())
                .build()
                .toUriString();

        Validator.validateUrlLength(url.length());
        return url;
    }
}
