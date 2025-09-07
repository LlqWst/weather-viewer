package dev.lqwd.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lqwd.interceptor.LoggingInterceptor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.*;

import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan({
        "dev.lqwd.controller",
        "dev.lqwd.interceptor",
        "dev.lqwd.exception_handler"
})
@PropertySource("classpath:app.properties")
public class WebMvcConfig implements WebMvcConfigurer {

    private static final int FIRST_INDEX = 0;

    @Value("${webMvc.corsRegistry.methods}")
    private String[] allowedMethods;

    @Value("${webMvc.corsRegistry.registryMapping}")
    private String registryMapping;

    @Value("${webMvc.corsRegistry.origins}")
    private String[] origins;

    @Value("${webMvc.encoding}")
    private String encoding;

    @Value("${webMvc.templateMode}")
    private String templateMode;

    @Value("${webMvc.suffix}")
    private String suffix;

    @Value("${webMvc.prefix}")
    private String prefix;

    @Value("${webMvc.isCacheable}")
    private boolean isCacheable;

    @Value("${webMvc.isSpringELCompiler}")
    private boolean isSpringELCompiler;

    public WebMvcConfig(LoggingInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    public Validator getValidator() {
        return validator();
    }

    private final LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/sign-in", "/sign-up", "/");
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {

        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();

        templateResolver.setPrefix(prefix);
        templateResolver.setSuffix(suffix);
        templateResolver.setTemplateMode(templateMode);
        templateResolver.setCharacterEncoding(encoding);
        templateResolver.setCacheable(isCacheable);

        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(isSpringELCompiler);

        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {

        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding(encoding);

        return viewResolver;
    }

    @Override
    public void configureDefaultServletHandling(@NonNull DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureViewResolvers(@NonNull ViewResolverRegistry registry) {
        registry.viewResolver(thymeleafViewResolver());
    }

    @Override
    public void extendMessageConverters(@NonNull List<HttpMessageConverter<?>> converters) {
        converters.add(FIRST_INDEX, new MappingJackson2HttpMessageConverter());
    }

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {

        registry.addMapping(registryMapping)
                .allowedOrigins(origins)
                .allowedMethods(allowedMethods);

    }
}
