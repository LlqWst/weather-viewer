package dev.lqwd.exception.api_weather_exception;

public class UnexpectedExternalApiException extends RuntimeException {
    public UnexpectedExternalApiException(String message) {
        super(message);
    }
}
