package dev.lqwd.exception.api_weather_exception;

public class InternalApiException extends RuntimeException {
    public InternalApiException(String message, Exception e) {
        super(message, e);
    }
    public InternalApiException(String message) {
        super(message);
    }
}
