package dev.lqwd.exception;

public class HttpClientException extends RuntimeException {
    public HttpClientException(String message, Exception e) {
        super(message, e);
    }
}
