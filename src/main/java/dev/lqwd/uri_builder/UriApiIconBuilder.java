package dev.lqwd.uri_builder;

import dev.lqwd.exception.BadRequestException;

public class UriApiIconBuilder extends AbstractUriApiBuilderImpl {

    private final static String URL_ICON = "https://openweathermap.org/img/wn/";
    private static final String SIZE_100_X_100PX = "@2x";
    private static final String EXTENSION = ".png";
    private final String iconCode;

    public UriApiIconBuilder(String iconCode) {
        super(URL_ICON);
        this.iconCode = iconCode;
    }

    @Override
    public String build() throws BadRequestException {
        String url = super.uriBuilder
                .pathSegment(iconCode + SIZE_100_X_100PX + EXTENSION)
                .build()
                .toUriString();

        super.validateUrlLength(url.length());
        return url;
    }
}
