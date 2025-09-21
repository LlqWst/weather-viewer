package dev.lqwd.configuration;


import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Slf4j
public class UriConfig {

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

}