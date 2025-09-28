package dev.lqwd.configuration.app_config;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
@Getter
@Slf4j
public class HttpClientConfig {

    private static final int TIMEOUT_SEC = 10;

    @Value("${APP_ID:}")
    private String appId;

    @PostConstruct
    public void validate() {
        if (appId.isEmpty()) {
            log.error("APP_ID not set");
            throw new IllegalStateException("APP_ID not set");
        }
        log.info("APP_ID configured successfully");
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SEC))
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}