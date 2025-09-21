package dev.lqwd.exception.api_weather_exception;

public class ApiException extends RuntimeException {
    public ApiException(String message, Exception e) {
        super(message, e);
    }
    public ApiException(String message) {
        super(message);
    }
}
