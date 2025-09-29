package dev.lqwd.configuration.web;


import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

@Configuration
@PropertySource("classpath:app.properties")
public class ThymeleafConfig implements WebMvcConfigurer {

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
    public void configureViewResolvers(@NonNull ViewResolverRegistry registry) {
        registry.viewResolver(thymeleafViewResolver());
    }

}
