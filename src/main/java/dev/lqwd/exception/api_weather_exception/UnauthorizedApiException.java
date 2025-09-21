package dev.lqwd.exception.api_weather_exception;

public class UnauthorizedApiException extends RuntimeException {
    public UnauthorizedApiException(String message, Exception e) {
        super(message, e);
    }
    public UnauthorizedApiException(String message) {
        super(message);
    }
}
