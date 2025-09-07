package dev.lqwd.service.weahter_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lqwd.dto.ApiLocationsResponseDTO;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class LocationService {

    private final ObjectMapper objectMapper;
    private static final String appid = System.getenv("APP_ID");
    private static final String URL = "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s";

    public LocationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<ApiLocationsResponseDTO> getLocations(String location){

        String paramURI = URL.formatted(location, appid);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(paramURI))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(
                    request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            List<ApiLocationsResponseDTO> locationDtos =  Arrays.asList(objectMapper.readValue(
                    response.body(), ApiLocationsResponseDTO[].class));

            System.out.println(locationDtos);

            return locationDtos;
        } catch (Exception e) {
            e.printStackTrace();
        }
            return Collections.emptyList();
    }

}
