package service_test;

import config.TestPersistenceConfig;
import dev.lqwd.dto.weather.api_response.ApiLocationResponseDTO;
import dev.lqwd.exception.BadRequestException;
import dev.lqwd.exception.api_weather_exception.ApiServiceUnavailableException;
import dev.lqwd.exception.api_weather_exception.SubscriptionApiException;
import dev.lqwd.exception.api_weather_exception.UnexpectedExternalApiException;
import dev.lqwd.service.weahter_api.ApiWeatherLocationServiceImpl;
import dev.lqwd.service.weahter_api.infrastructure.ApiHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestPersistenceConfig.class})
@ActiveProfiles("test")
class WeatherLocationServiceTest {

    private static final String TEST_URI = "https://test.com";
    private static final String TEST_NAME = "test_city";
    private static final BigDecimal TEST_LAT = BigDecimal.valueOf(30.3162);
    private static final BigDecimal TEST_LON = BigDecimal.valueOf(59.9387);
    private static final String TEST_COUNTRY = "test_county";
    private static final String TEST_STATE = "test_state";
    private static final String MESSAGE_ERROR = "Message error";


    @MockitoBean
    private ApiHttpClient apiHttpClient;

    @Autowired
    private ApiWeatherLocationServiceImpl apiWeatherLocationService;

    @Test
    public void should_deserializeJsonLocation_after_getRequest() {

        String mockJsonResponse = """
                {
                    "name":"%s",
                    "lat":%s,
                    "lon":%s,
                    "country":"%s",
                    "state":"%s"
                }
                """.formatted(TEST_NAME, TEST_LAT.toPlainString(), TEST_LON.toPlainString(), TEST_COUNTRY, TEST_STATE);

        List<ApiLocationResponseDTO> deserializedMockResponse = List.of(
                ApiLocationResponseDTO.builder()
                        .name(TEST_NAME)
                        .lat(TEST_LAT)
                        .lon(TEST_LON)
                        .country(TEST_COUNTRY)
                        .state(TEST_STATE)
                        .build()
        );

        HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
        when(mockHttpResponse.body()).thenReturn(mockJsonResponse);
        when(mockHttpResponse.statusCode()).thenReturn(SC_OK);
        when(apiHttpClient.executeGetRequest(TEST_URI)).thenReturn(mockHttpResponse);

        List<ApiLocationResponseDTO> result = apiWeatherLocationService.fetchApiData(TEST_URI);

        Assertions.assertNotNull(result);
        assertEquals(deserializedMockResponse, result);
    }


    @Test
    public void should_throwException_after_ResponseWithCode400() {

        should_throwException_after_ResponseWithCode(SC_BAD_REQUEST, BadRequestException.class);
    }

    @Test
    public void should_throwException_after_ResponseWithCode401() {

        should_throwException_after_ResponseWithCode(SC_UNAUTHORIZED, SubscriptionApiException.class);
    }

    @Test
    public void should_throwException_after_ResponseWithCode500() {

        should_throwException_after_ResponseWithCode(SC_INTERNAL_SERVER_ERROR, ApiServiceUnavailableException.class);
    }

    @Test
    public void should_throwException_after_ResponseWithUnexpectedCode() {

        int unexpectedCode = SC_CONFLICT;
        should_throwException_after_ResponseWithCode(unexpectedCode, UnexpectedExternalApiException.class);
    }

    private void should_throwException_after_ResponseWithCode(int code, Class<? extends Exception> ExceptionClass) {

        String mockJsonResponse = """
                {
                    "code":%d,
                    "message":"%s"
                }
                """.formatted(code, MESSAGE_ERROR);

        HttpResponse<String> mockHttpResponse = mock(HttpResponse.class);
        when(mockHttpResponse.body()).thenReturn(mockJsonResponse);
        when(mockHttpResponse.statusCode()).thenReturn(code);
        when(apiHttpClient.executeGetRequest(TEST_URI)).thenReturn(mockHttpResponse);


        assertThrows(ExceptionClass,
                () -> apiWeatherLocationService.fetchApiData(TEST_URI));

    }

}
