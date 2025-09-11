package dev.lqwd.uri_builder;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

public abstract class AbstractUriApiBuilderImpl implements AbstractUriApiBuilder {
    protected final String appId;
    protected final UriComponentsBuilder uriBuilder;

    protected AbstractUriApiBuilderImpl(String url) {
        this.appId = Objects.requireNonNull(
                System.getenv("APP_ID"),
                "APP_ID environment variable is not set");
        this.uriBuilder = UriComponentsBuilder.fromUriString(url);
    }

    public abstract String build();
}