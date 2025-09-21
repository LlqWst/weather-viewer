package dev.lqwd.service.weahter_api.infrastructure;

import dev.lqwd.dto.weather.api_response.ApiErrorResponse;
import dev.lqwd.exception.BadRequestException;
import dev.lqwd.exception.api_weather_exception.DataRangeApiException;
import dev.lqwd.exception.api_weather_exception.InternalApiException;
import dev.lqwd.exception.api_weather_exception.SubscriptionApiException;
import dev.lqwd.exception.api_weather_exception.UnauthorizedApiException;
import org.springframework.stereotype.Component;


import static jakarta.servlet.http.HttpServletResponse.*;

@Component
public class ApiExceptionHandler {

    private static final int FORBIDDEN_DATA_RANGE = 400000;
    private static final int EXCEEDED_LIMIT_OF_SUBSCRIPTION = 429;

    public void validate(ApiErrorResponse error) {
        int statusCode = error.getCode();
        String body = error.getMessage();

        switch (statusCode) {
            case FORBIDDEN_DATA_RANGE ->
                    throw new DataRangeApiException("Request data for a period not covered by subscription body: " + body);
            case SC_UNAUTHORIZED ->
                    throw new UnauthorizedApiException("Error due to incorrect or blank API key body: " + body);
            case SC_NOT_FOUND -> throw new BadRequestException("Request is incorrect. Check URI. body: " + body);
            case EXCEEDED_LIMIT_OF_SUBSCRIPTION ->
                    throw new SubscriptionApiException("Request is incorrect. Check URI. body: " + body);
            case SC_INTERNAL_SERVER_ERROR,
                 SC_BAD_GATEWAY,
                 SC_SERVICE_UNAVAILABLE,
                 SC_GATEWAY_TIMEOUT -> throw new InternalApiException("Internal API error. body: " + body);
            default ->
                    throw new BadRequestException("Unexpected api error. Code: %d, body: %s".formatted(statusCode, body));
        }
    }

}
