package dev.lqwd.configuration.web;

import dev.lqwd.interceptor.AuthInterceptor;
import lombok.NonNull;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.*;


@Configuration
@EnableWebMvc
@ComponentScan({
        "dev.lqwd.configuration.app_config",
        "dev.lqwd.configuration.persistence",
        "dev.lqwd.configuration.web",
        "dev.lqwd.controller",
        "dev.lqwd.interceptor",
        "dev.lqwd.exception_handler",
        "dev.lqwd.mapper",
        "dev.lqwd.service",
        "dev.lqwd.repository"
})
@PropertySource("classpath:app.properties")
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebMvcConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/error/**",
                        "/images/**",
                        "/css/**",
                        "/js/**",
                        "/static/**",
                        "/webjars/**",
                        "/favicon.ico"
                );
    }

    @Override
    public void configureDefaultServletHandling(@NonNull DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

}
