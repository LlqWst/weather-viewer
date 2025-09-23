package service_test;

import dev.lqwd.dto.weather.api_response.ApiLocationResponseDTO;
import dev.lqwd.service.weahter_api.ApiWeatherLocationServiceImpl;
import dev.lqwd.service.weahter_api.infrastructure.ApiHttpClient;
import dev.lqwd.service.weahter_api.infrastructure.ExternalApiExceptionHandler;
import dev.lqwd.service.weahter_api.infrastructure.JsonDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherLocationServiceTest {


    public static final String TEST_URI = "test_uri";
    public static final String TEST_NAME = "test_city";
    public static final BigDecimal TEST_LAT = BigDecimal.valueOf(50);
    public static final BigDecimal TEST_LON = BigDecimal.valueOf(80);
    public static final String TEST_COUNTRY = "test_county";
    public static final String TEST_STATE = "test_state";

    @Mock
    private JsonDeserializer jsonDeserializer;

    @Mock
    private ApiHttpClient apiHttpClient;

    @Mock
    private ExternalApiExceptionHandler externalApiExceptionHandler;

    @InjectMocks
    private ApiWeatherLocationServiceImpl weatherLocationService;


    @Test
    public void should_deserializeJsonLocation_after_getRequest() {

        String mockJsonResponse = """
                {
                    "name":"%s",
                    "lat":"%bd",
                    "lon":"%bd",
                    "country":"%s",
                    "state":"%s"
                }
                """.formatted(TEST_NAME, TEST_LAT, TEST_LON, TEST_COUNTRY, TEST_STATE);

        List<ApiLocationResponseDTO> deserializedResponse = List.of(
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

        when(apiHttpClient.executeRequest(TEST_URI)).thenReturn(mockHttpResponse);

        when(jsonDeserializer.deserialize(mockJsonResponse, ApiLocationResponseDTO.class))
                .thenReturn(deserializedResponse);

        List<ApiLocationResponseDTO> result = weatherLocationService.fetchApiData(TEST_URI);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(TEST_NAME, result.get(0).getName());

        verify(jsonDeserializer).deserialize(mockJsonResponse, ApiLocationResponseDTO.class);
    }

}
