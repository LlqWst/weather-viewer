package dev.lqwd.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Arrays;
import java.util.List;

@Configuration
@PropertySource("classpath:app.properties")
@Setter
@Getter
public class AuthConfig {

    @Value("${webMvc.corsRegistry.applicationBasePath}")
    private String applicationBasePath;

    private String baseUrl;
    private String homeUrl;
    private String signInUrl;
    private List<String> publicUrls;

    @PostConstruct
    public void init() {
        this.baseUrl = "/" + applicationBasePath;
        this.homeUrl = baseUrl + "/";
        this.signInUrl = baseUrl + "/sign-in";
        this.publicUrls = Arrays.asList(
                signInUrl,
                baseUrl + "/sign-up"
        );
    }
}
