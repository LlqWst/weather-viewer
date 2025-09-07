package dev.lqwd.service.weahter_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lqwd.dto.ApiWeatherResponseDTO;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

@Service
public class WeatherService {

    private final ObjectMapper objectMapper;

    public WeatherService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Optional<ApiWeatherResponseDTO> getWeather(){
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openweathermap.org/data/2.5/weather?lat=51.5073219&lon=-0.1276474&appid=99ba3c668efc7b2d76880b1b7083dee9"))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(
                    request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            ApiWeatherResponseDTO locationDto = objectMapper.readValue(
                    response.body(), ApiWeatherResponseDTO.class);

            System.out.println(locationDto.toString());

            return Optional.of(locationDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
            return Optional.empty();
    }

}
