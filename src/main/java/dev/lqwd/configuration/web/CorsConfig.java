package dev.lqwd.configuration.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${webMvc.corsRegistry.methods}")
    private String[] allowedMethods;

    @Value("${webMvc.corsRegistry.registryMapping}")
    private String registryMapping;

    @Value("${webMvc.corsRegistry.origins}")
    private String[] origins;

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping(registryMapping)
                .allowedOrigins(origins)
                .allowedMethods(allowedMethods);
    }
}
