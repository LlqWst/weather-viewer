package dev.lqwd.uri_builder;

public class UriApiLocationBuilder extends AbstractUriApiBuilderImpl {

    private static final String URL_LOCATIONS = "https://api.openweathermap.org/geo/1.0/direct";
    private static final int MAX_LIMIT = 5;
    private final String location;

    public UriApiLocationBuilder(String location) {
        super(URL_LOCATIONS);
        this.location = location;
    }

    @Override
    public String build() {
        return super.uriBuilder
                .queryParam("q", location)
                .queryParam("limit", MAX_LIMIT)
                .queryParam("appid", AbstractUriApiBuilderImpl.APP_ID)
                .build()
                .toUriString();
    }
}
