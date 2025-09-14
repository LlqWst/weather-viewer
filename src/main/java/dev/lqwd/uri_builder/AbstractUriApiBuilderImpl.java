package dev.lqwd.uri_builder;

import dev.lqwd.exception.BadRequestException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

public abstract class AbstractUriApiBuilderImpl implements AbstractUriApiBuilder {
    private static final int MAX_LENGTH_URL = 2000;
    private static final String ERROR_MESSAGE_TOO_LONG_URL = "URL length has more than %d symbols: %d";
    protected static final String APP_ID = Objects.requireNonNull(
            System.getenv("APP_ID"),
            "APP_ID environment variable is not set");

    protected final UriComponentsBuilder uriBuilder;

    protected AbstractUriApiBuilderImpl(String url) {
        this.uriBuilder = UriComponentsBuilder.fromUriString(url);
    }

    protected void validateUrlLength(int urlLength){
        if (MAX_LENGTH_URL < urlLength){
            throw new BadRequestException(ERROR_MESSAGE_TOO_LONG_URL
                    .formatted(MAX_LENGTH_URL, urlLength));
        }
    }

    public abstract String build();
}