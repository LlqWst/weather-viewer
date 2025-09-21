package dev.lqwd.exception.api_weather_exception;

public class SubscriptionApiException extends RuntimeException {
    public SubscriptionApiException(String message, Exception e) {
        super(message, e);
    }
    public SubscriptionApiException(String message) {
        super(message);
    }
}
