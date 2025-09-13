package dev.lqwd.uri_builder;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

public abstract class AbstractUriApiBuilderImpl implements AbstractUriApiBuilder {
    protected static final String APP_ID = Objects.requireNonNull(
            System.getenv("APP_ID"),
            "APP_ID environment variable is not set");

    protected final UriComponentsBuilder uriBuilder;

    protected AbstractUriApiBuilderImpl(String url) {
        this.uriBuilder = UriComponentsBuilder.fromUriString(url);
    }

    public abstract String build();
}