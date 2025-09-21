package dev.lqwd.exception.api_weather_exception;

public class ApiServiceUnavailableException extends RuntimeException {
    public ApiServiceUnavailableException(String message) {
        super(message);
    }
}
