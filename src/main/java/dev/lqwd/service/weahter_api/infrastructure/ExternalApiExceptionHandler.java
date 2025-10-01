package dev.lqwd.service.weahter_api.infrastructure;

import dev.lqwd.dto.weather.api_response.ApiErrorResponse;
import dev.lqwd.exception.BadRequestException;
import dev.lqwd.exception.api_weather_exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.*;

@Component
@Slf4j
public class ExternalApiExceptionHandler {

    private static final int FORBIDDEN_DATA_RANGE = 400000;
    private static final int EXCEEDED_LIMIT_OF_SUBSCRIPTION = 429;
    private static final String MESSAGE_SERVICE_UNAVAILABLE = "Service temporarily unavailable. Please try again later.";
    private static final String MESSAGE_INCORRECT_REQUEST = "Incorrect data was provided to complete the request";

    public void handle(List<ApiErrorResponse> error) {
        if (error.isEmpty()) {
            throw new UnexpectedExternalApiException(MESSAGE_SERVICE_UNAVAILABLE);
        }

        ApiErrorResponse errorResponse = error.get(0);
        int statusCode = errorResponse.code();
        String message = errorResponse.message();

        log.warn("External API error: {} - {}", statusCode, message);
        switch (statusCode) {
            case FORBIDDEN_DATA_RANGE,
                 SC_UNAUTHORIZED,
                 SC_FORBIDDEN,
                 EXCEEDED_LIMIT_OF_SUBSCRIPTION -> throw new SubscriptionApiException(MESSAGE_SERVICE_UNAVAILABLE);

            case SC_BAD_REQUEST -> throw new BadRequestException(MESSAGE_INCORRECT_REQUEST);

            case SC_INTERNAL_SERVER_ERROR,
                 SC_BAD_GATEWAY,
                 SC_SERVICE_UNAVAILABLE,
                 SC_GATEWAY_TIMEOUT -> throw new ApiServiceUnavailableException(MESSAGE_SERVICE_UNAVAILABLE);

            default -> throw new UnexpectedExternalApiException(MESSAGE_SERVICE_UNAVAILABLE);
        }
    }

}
