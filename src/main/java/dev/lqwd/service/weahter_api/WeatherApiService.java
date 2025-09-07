package dev.lqwd.service.weahter_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lqwd.dto.ApiWeatherResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
public abstract class WeatherApiService<T> {

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected static final String APP_ID = System.getenv("APP_ID");

//    public WeatherApiService(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }

    public List<T> getWeather(String uri, Class<T> clazz){

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(
                    request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());

            if (!clazz.isArray()){

                return Collections.singletonList(
                        objectMapper.readValue(response.body(), clazz));
            }

            List<T> locationDto= Arrays.asList(
                    objectMapper.readValue(response.body(),
                    objectMapper.getTypeFactory().constructArrayType(clazz)));

            System.out.println(locationDto);

            return locationDto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
