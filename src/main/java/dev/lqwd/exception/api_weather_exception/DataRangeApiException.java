package dev.lqwd.exception.api_weather_exception;

public class DataRangeApiException extends RuntimeException {
    public DataRangeApiException(String message, Exception e) {
        super(message, e);
    }
    public DataRangeApiException(String message) {
        super(message);
    }
}
