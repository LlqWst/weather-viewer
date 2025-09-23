package dev.lqwd.uri_api_builder;

import dev.lqwd.configuration.HttpClientConfig;
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
    private final HttpClientConfig httpClientConfig;

    public String build(String location) throws BadRequestException {
        String url = UriComponentsBuilder.fromUriString(URL_LOCATIONS)
                .queryParam("q", location)
                .queryParam("limit", MAX_LIMIT)
                .queryParam("appid", httpClientConfig.getAppId())
                .build()
                .toUriString();

        Validator.validateUrlLength(url.length());
        return url;
    }
}
