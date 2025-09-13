package dev.lqwd.uri_builder;

import dev.lqwd.entity.Location;
import dev.lqwd.exception.BadRequestException;

public class UriApiWeatherBuilder extends AbstractUriApiBuilderImpl {

    private static final String URL_WEATHER = "https://api.openweathermap.org/data/2.5/weather";
    private static final String UNITS = "metric";
    private final Location location;

    public UriApiWeatherBuilder(Location location) {
        super(URL_WEATHER);
        this.location = location;
    }

    @Override
    public String build() throws BadRequestException {
        String url = super.uriBuilder
                .queryParam("lat", location.getLatitude())
                .queryParam("lon", location.getLongitude())
                .queryParam("appid", AbstractUriApiBuilderImpl.APP_ID)
                .queryParam("units", UNITS)
                .build()
                .toUriString();

        super.validateUrlLength(url.length());
        return url;
    }
}
